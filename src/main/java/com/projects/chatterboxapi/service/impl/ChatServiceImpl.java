package com.projects.chatterboxapi.service.impl;

import com.google.gson.Gson;
import com.projects.chatterboxapi.dto.ChatControllerMessage;
import com.projects.chatterboxapi.dto.Message;
import com.projects.chatterboxapi.dto.PubSubMessage;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.repository.RoomRepository;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.ChatService;
import com.projects.chatterboxapi.service.RedisMessageSubscriber;
import com.projects.chatterboxapi.utils.MessageType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatServiceImpl.class);
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    ChannelTopic topic;
    MessageListenerAdapter messageListenerAdapter;

    @Override
    public SseEmitter streamSseMvc(int userId) {
        AtomicBoolean isComplete = new AtomicBoolean(false);
        SseEmitter emitter = new SseEmitter();
        Function<String, Integer> handler = (String message) -> {
            SseEmitter.SseEventBuilder event = SseEmitter
                    .event()
                    .data(message);
            try {
                emitter.send(event);
            } catch (IOException e) {
                return 1;
            }
            return 0;
        };

        RedisMessageSubscriber redisMessageSubscriber = (RedisMessageSubscriber) messageListenerAdapter.getDelegate();
        redisMessageSubscriber.attach(handler);
        Runnable onDetach = () -> {
            redisMessageSubscriber.detach(handler);
            if (!isComplete.get()) {
                isComplete.set(true);
                emitter.complete();
            }
        };

        emitter.onCompletion(onDetach);
        emitter.onError(err -> onDetach.run());
        emitter.onTimeout(onDetach);
        return emitter;
    }

    @Override
    public void get(ChatControllerMessage chatControllerMessage) {
        Gson gson = new Gson();
        String serializedMessage;
        LOGGER.info("Received message: {}", chatControllerMessage.toString());
        if (chatControllerMessage.getType() == MessageType.MESSAGE) {
            serializedMessage = handleRegularMessageCase(chatControllerMessage);
        } else if(chatControllerMessage.getType() == MessageType.USER_CONNECTED
                || chatControllerMessage.getType() == MessageType.USER_DISCONNECTED) {
            serializedMessage = handleUserConnectionCase(chatControllerMessage);
        } else {
            serializedMessage = gson.toJson(new PubSubMessage<>(chatControllerMessage.getType().getValue(), chatControllerMessage));
        }

        roomRepository.sendMessageToRedis(topic.getTopic(), serializedMessage);
    }

    private String handleRegularMessageCase(ChatControllerMessage chatMessage) {
        Gson gson = new Gson();
        Message message = gson.fromJson(chatMessage.getData(), Message.class);
        userRepository.addUserToOnlineList(message.getFrom());
        roomRepository.saveMessage(message);
        return gson.toJson(new PubSubMessage<>(chatMessage.getType().getValue(), message));
    }

    private String handleUserConnectionCase(ChatControllerMessage chatControllerMessage) {
        Gson gson = new Gson();
        int userId = chatControllerMessage.getUser().getId();
        String messageType = chatControllerMessage.getType().getValue();
        User serializedUser = gson.fromJson(chatControllerMessage.getData(), User.class);
        String serializedMessage = gson.toJson(new PubSubMessage<>(messageType, serializedUser));
        if (chatControllerMessage.getType() == MessageType.USER_CONNECTED) {
            userRepository.addUserToOnlineList(String.valueOf(userId));
        } else {
            userRepository.removeUserFromOnlineList(String.valueOf(userId));
        }

        return serializedMessage;
    }
}
