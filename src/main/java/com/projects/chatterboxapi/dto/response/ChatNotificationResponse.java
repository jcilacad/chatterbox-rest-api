package com.projects.chatterboxapi.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationResponse {

    private String id;
    private String senderId;
    private String senderName;
}
