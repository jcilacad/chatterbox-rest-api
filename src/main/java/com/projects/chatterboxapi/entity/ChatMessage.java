package com.projects.chatterboxapi.entity;

import com.projects.chatterboxapi.enums.MessageStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    private String chatId;

    private String senderId;

    private String recipientId;

    private String content;

    private Date timestamp;

    private MessageStatus status;
}
