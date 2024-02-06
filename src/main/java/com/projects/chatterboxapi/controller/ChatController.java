package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.entity.ChatMessage;
import com.projects.chatterboxapi.service.ChatMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/messages")
public class ChatController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findMessage(@PathVariable String id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        chatMessageService.processMessage(chatMessage);
    }

    @GetMapping("/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.countNewMessage(senderId, recipientId));
    }

    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages(@PathVariable String senderId,
                                              @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
