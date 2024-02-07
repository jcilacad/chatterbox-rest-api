package com.projects.chatterboxapi.repository;

import com.projects.chatterboxapi.entity.ChatMessage;
import com.projects.chatterboxapi.service.ChatRoomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@Slf4j
public class ChatMessageRepositoryTest {

    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageRepositoryTest(ChatRoomService chatRoomService, ChatMessageRepository chatMessageRepository) {
        this.chatRoomService = chatRoomService;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Test
    void processMessage() {
        String senderId = "101001727852120435757";
        String recipientId = "108977918358293537943";
        String senderName = "John Christopher Ilacad";
        String recipientName = "John Christopher Ilacad";
        var chatId = chatRoomService.getChatId(senderId, recipientId, true);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(chatId.get());
        chatMessage.setSenderId(senderId);
        chatMessage.setRecipientId(recipientId);
        chatMessage.setSenderName(senderName);
        chatMessage.setRecipientName(recipientName);
        chatMessage.setTimestamp(new Date());
        chatMessage.setContent("Message 3");
        chatMessageRepository.save(chatMessage);
    }
}
