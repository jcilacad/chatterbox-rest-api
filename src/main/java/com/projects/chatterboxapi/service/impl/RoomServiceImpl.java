package com.projects.chatterboxapi.service.impl;

import com.google.gson.Gson;
import com.projects.chatterboxapi.dto.Message;
import com.projects.chatterboxapi.entity.Room;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.repository.RoomRepository;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.RoomService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomServiceImpl.class);
    private RoomRepository roomRepository;
    private UserRepository userRepository;

    @Override
    public List<Room> getRooms(int userId) {
        Set<String> roomIds = roomRepository.getUserRoomIds(userId);
        if (roomIds == null) {
            // TODO: Create a custom error response (Bad Request)
            throw new RuntimeException("Bad Request");
        }

        List<Room> rooms = new ArrayList<>();
        // TODO: Refactor this, use stream
        for (String roomId : roomIds) {
            boolean roomExists = roomRepository.isRoomExists(roomId);
            if (roomExists){
                String name = roomRepository.getRoomNameById(roomId);
                if (name == null) {
                    Room privateRoom = handlePrivateRoomCase(roomId);
                    if (privateRoom == null){
                        // TODO: Create a custom error response (Internal Server Error)
                        throw new RuntimeException("Internal Server Error");
                    }
                    rooms.add(privateRoom);
                } else {
                    rooms.add(new Room(roomId, name));
                }
            }
        }
        return rooms;
    }

    @Override
    public List<Message> getMessages(String roomId, int offset, int size) {
        boolean roomExists = roomRepository.isRoomExists(roomId);
        List<Message> messages = new ArrayList<>();
        if (roomExists) {
            Set<String> values = roomRepository.getMessages(roomId, offset, size);
            for (String value : values) {
                messages.add(deserialize(value));
            }

        }

        return messages;
    }

    private String[] parseUserIds(String roomId){
        String[] userIds = roomId.split(":");
        if (userIds.length != 2){
            LOGGER.error("User ids not parsed properly");
            throw new RuntimeException(String.format("Unable to parse users ids from roomId: %s", roomId));
        }

        return userIds;
    }

    private Room handlePrivateRoomCase(String roomId){
        String[] userIds = parseUserIds(roomId);
        User firstUser = userRepository.getUserById(Integer.parseInt(userIds[0]));
        User secondUser = userRepository.getUserById(Integer.parseInt(userIds[1]));
        if (firstUser == null || secondUser == null){
            LOGGER.error("Users were not found by ids: {}", Arrays.toString(userIds));
            return null;
        }

        return new Room(roomId, firstUser.getUsername(), secondUser.getUsername());
    }

    private Message deserialize(String value){
        Gson gson = new Gson();
        try {
            return gson.fromJson(value, Message.class);
        } catch (Exception e) {
            LOGGER.error(String.format("Couldn't deserialize json: %s", value), e);
        }

        return null;
    }
}
