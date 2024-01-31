package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.entity.ChatMessage;
import com.projects.chatterboxapi.enums.MessageStatus;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    long countNewMessage(String senderId, String recipientId);

    List<ChatMessage> findChatMessages(String senderId, String recipientId);

    ChatMessage findById(String id);

    void updateStatuses(String senderId, String recipientId, MessageStatus status);

    void processMessage(ChatMessage chatMessage);
}
