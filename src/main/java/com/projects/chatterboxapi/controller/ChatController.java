package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.response.ChatNotificationResponse;
import com.projects.chatterboxapi.entity.ChatMessage;
import com.projects.chatterboxapi.service.ChatMessageService;
import com.projects.chatterboxapi.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.projects.chatterboxapi.constants.OpenApiConstants.*;
import static com.projects.chatterboxapi.constants.PathConstants.*;

@Tag(name = CHAT_CONTROLLER_TAG)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@Controller
@AllArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping(CHAT)
    public void processMessage(@Payload ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());
        ChatMessage savedMessage = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                QUEUE_MESSAGES, new ChatNotificationResponse(
                        savedMessage.getId(),
                        savedMessage.getSenderId(),
                        savedMessage.getSenderName()));
    }

    @Operation(summary = COUNT_NEW_MESSAGES)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @GetMapping(SENDER_ID_RECIPIENT_ID_COUNT)
    public ResponseEntity<Long> countNewMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.countNewMessage(senderId, recipientId));
    }

    @Operation(summary = FIND_CHAT_MESSAGES)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @GetMapping(SENDER_ID_RECIPIENT_ID)
    public ResponseEntity<?> findChatMessages(@PathVariable String senderId,
                                              @PathVariable String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @Operation(summary = FIND_MESSAGES)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @GetMapping(ID)
    public ResponseEntity<?> findMessage(@PathVariable String id) {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }
}
