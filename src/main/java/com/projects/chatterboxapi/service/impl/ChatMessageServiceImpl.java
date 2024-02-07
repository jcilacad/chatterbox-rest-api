package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.entity.ChatMessage;
import com.projects.chatterboxapi.dto.response.ChatNotificationResponse;
import com.projects.chatterboxapi.enums.MessageStatus;
import com.projects.chatterboxapi.exception.ResourceNotFoundException;
import com.projects.chatterboxapi.repository.ChatMessageRepository;
import com.projects.chatterboxapi.service.ChatMessageService;
import com.projects.chatterboxapi.service.ChatRoomService;
import com.projects.chatterboxapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;

    @Override
    @Transactional
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
    @Transactional
    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        chatMessages.forEach(chatMessage -> chatMessage.setStatus(status));
        chatMessageRepository.saveAll(chatMessages);
    }

    @Override
    @Transactional
    public void processMessage(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                new ChatNotificationResponse(
                        savedMessage.getId(),
                        savedMessage.getSenderId(),
                        savedMessage.getSenderName()));
    }
}
