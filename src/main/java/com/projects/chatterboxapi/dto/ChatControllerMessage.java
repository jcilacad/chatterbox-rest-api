package com.projects.chatterboxapi.dto;

import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.utils.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatControllerMessage {
    private MessageType type;
    private User user;
    private String data;
}
