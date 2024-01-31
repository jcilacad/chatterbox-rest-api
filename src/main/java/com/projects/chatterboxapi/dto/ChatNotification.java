package com.projects.chatterboxapi.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {

    private String id;

    private String senderId;

    private String senderName;
}
