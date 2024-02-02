package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.dto.response.ChatMessageResponse;
import com.projects.chatterboxapi.dto.response.MessengerResponse;
import com.projects.chatterboxapi.entity.ChatMessage;
import com.projects.chatterboxapi.dto.response.ChatNotificationResponse;
import com.projects.chatterboxapi.enums.MessageStatus;
import com.projects.chatterboxapi.exception.ResourceNotFoundException;
import com.projects.chatterboxapi.mapper.ChatMessageMapper;
import com.projects.chatterboxapi.repository.ChatMessageRepository;
import com.projects.chatterboxapi.service.ChatMessageService;
import com.projects.chatterboxapi.service.ChatRoomService;
import com.projects.chatterboxapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final MongoOperations mongoOperations;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public long countNewMessage(String senderId, String recipientId) {
        return chatMessageRepository
                .countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);
        var messages = chatId.map(cId -> chatMessageRepository.findByChatId(cId)).orElse(new ArrayList<>());

        if (messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }
        return messages;
    }

    @Override
    public ChatMessage findById(String id) {
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "Id", id));
    }

    @Override
    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }

    @Override
    public void processMessage(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotificationResponse(
                        savedMessage.getId(),
                        savedMessage.getSenderId(),
                        savedMessage.getSenderId()
                )
        );
    }

    @Override
    public MessengerResponse messengerResponse(String senderId, String recipientId, String name) {
        UserRequest loggedInUser = (UserRequest) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserRequest> userRequests = getUsers(name);
        List<ChatMessageResponse> chatMessageResponses = getChatMessageResponses(senderId, recipientId);
        return new MessengerResponse(loggedInUser, userRequests, chatMessageResponses);
    }

    private List<UserRequest> getUsers(String name) {
        if (name != null) {
            return userService.getUsersByName(name);
        }
        return userService.getUsers();
    }

    private List<ChatMessageResponse> getChatMessageResponses(String senderId, String recipientId) {
        if (isValidInput(senderId, recipientId)) {
            List<ChatMessage> chatMessages = findChatMessages(senderId, recipientId);
            return chatMessages.stream()
                    .map(chatMessage -> ChatMessageMapper.MAPPER.toDto(chatMessage))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private boolean isValidInput(String... values) {
        return Arrays.stream(values).allMatch(value -> value != null);
    }
}
