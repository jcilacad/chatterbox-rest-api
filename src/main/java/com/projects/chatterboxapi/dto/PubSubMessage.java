package com.projects.chatterboxapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PubSubMessage<T> {
    private String type;
    private T data;
}
