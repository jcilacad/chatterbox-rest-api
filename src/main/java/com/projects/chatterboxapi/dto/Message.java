package com.projects.chatterboxapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Message {
    private String from;
    private int date;
    private String message;
    private String roomId;
}
