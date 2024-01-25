package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.ChatControllerMessage;
import com.projects.chatterboxapi.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private ChatService chatService;

    @RequestMapping("/stream")
    public SseEmitter streamSseMvc(@RequestParam int userId) {
        return chatService.streamSseMvc(userId);
    }

    @RequestMapping(value = "/emit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> get(@RequestBody ChatControllerMessage chatControllerMessage) {
        chatService.get(chatControllerMessage);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
