package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.ChatControllerMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ChatService {
    SseEmitter streamSseMvc(int userId);
    void get(ChatControllerMessage chatControllerMessage);
}
