package com.projects.chatterboxapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    MESSAGE("message"),
    USER_CONNECTED("user.connected"),
    USER_DISCONNECTED("user.disconnected");

    private String value;
}
