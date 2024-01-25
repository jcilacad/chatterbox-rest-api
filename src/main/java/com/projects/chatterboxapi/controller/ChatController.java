package com.projects.chatterboxapi.controller;

import com.google.gson.Gson;
import com.projects.chatterboxapi.dto.ChatControllerMessage;
import com.projects.chatterboxapi.dto.Message;
import com.projects.chatterboxapi.dto.PubSubMessage;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.repository.RoomRepository;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.RedisMessageSubscriber;
import com.projects.chatterboxapi.utils.MessageType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    ChannelTopic topic;
    MessageListenerAdapter messageListenerAdapter;

    @RequestMapping("/stream")
    public SseEmitter streamSseMvc(@RequestParam int userId) {
        AtomicBoolean isComplete = new AtomicBoolean(false);
        SseEmitter emitter = new SseEmitter();

        Function<String, Integer> handler = (String message) -> {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
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

    @RequestMapping(value = "/emit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> get(@RequestBody ChatControllerMessage chatControllerMessage) {
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
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private String handleRegularMessageCase(ChatControllerMessage chatMessage){
        Gson gson = new Gson();
        // We've received a message from user. It's necessary to deserialize it first.
        Message message = gson.fromJson(chatMessage.getData(), Message.class);
        // Add the user who sent the message to online list.
        userRepository.addUserToOnlineList(message.getFrom());
        //redisTemplate.opsForSet().add(ONLINE_USERS_KEY, message.getFrom());
        // Write the message to DB.
        roomRepository.saveMessage(message);
        // Finally create the serialized output which would go to pub/sub
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
