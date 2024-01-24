package com.projects.chatterboxapi.repository;

import com.google.gson.Gson;
import com.projects.chatterboxapi.dto.Message;
import com.projects.chatterboxapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@AllArgsConstructor
public class RoomRepository {
    private static final Logger logger = LoggerFactory.getLogger(RoomRepository.class);
    private StringRedisTemplate redisTemplate;

    public Set<String> getUserRoomIds(int userId) {
        String userRoomKey = String.format(AppConstants.USER_ROOMS_KEY, userId);
        Set<String> roomIds = redisTemplate.opsForSet().members(userRoomKey);
        logger.debug("Received roomIds by userId: {}", userId);
        return roomIds;
    }

    public boolean isRoomExists(String roomId) {
        return redisTemplate.hasKey(String.format(AppConstants.ROOM_KEY, roomId));
    }

    public String getRoomNameById(String roomId) {
        String roomNameKey = String.format(AppConstants.ROOM_NAME_KEY, roomId);
        return redisTemplate.opsForValue().get(roomNameKey);
    }

    public  Set<String> getMessages(String roomId, int offset, int size) {
        String roomNameKey = String.format(AppConstants.ROOM_KEY, roomId);
        Set<String> messages = redisTemplate.opsForZSet().reverseRange(roomNameKey, offset, offset + size);
        logger.debug(String.format("Received messages by roomId:%s, offset:%s, size:%s ", roomId, offset, size));
        return messages;
    }

    public void sendMessageToRedis(String topic, String serializedMessage) {
        logger.debug(String.format("Saving message to Redis: topic:%s, message:%s ", topic, serializedMessage));
        redisTemplate.convertAndSend(topic, serializedMessage);
    }

    public void saveMessage(Message message) {
        Gson gson = new Gson();
        String roomKey = String.format(AppConstants.ROOM_KEY, message.getRoomId());
        redisTemplate.opsForZSet().add(roomKey, gson.toJson(message), message.getDate());
    }
}
