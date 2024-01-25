package com.projects.chatterboxapi.controller;

import com.google.gson.Gson;
import com.projects.chatterboxapi.dto.Message;
import com.projects.chatterboxapi.entity.Room;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.repository.RoomRepository;
import com.projects.chatterboxapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);
    private RoomRepository roomRepository;
    private UserRepository userRepository;

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Room>> getRooms(@PathVariable int userId) {
        Set<String> roomIds = roomRepository.getUserRoomIds(userId);
        if (roomIds == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Room> rooms = new ArrayList<>();

        for (String roomId : roomIds) {
            boolean roomExists = roomRepository.isRoomExists(roomId);
            if (roomExists){
                String name = roomRepository.getRoomNameById(roomId);
                if (name == null) {
                    // private chat case
                    Room privateRoom = handlePrivateRoomCase(roomId);
                    if (privateRoom == null){
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    rooms.add(privateRoom);
                } else {
                    rooms.add(new Room(roomId, name));
                }
            }
        }
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    private String[] parseUserIds(String roomId){
        String[] userIds = roomId.split(":");
        if (userIds.length != 2){
            LOGGER.error("User ids not parsed properly");
            throw new RuntimeException("Unable to parse users ids from roomId: "+roomId);
        }
        return userIds;
    }

    private Room handlePrivateRoomCase(String roomId){
        String[] userIds = parseUserIds(roomId);
        User firstUser = userRepository.getUserById(Integer.parseInt(userIds[0]));
        User secondUser = userRepository.getUserById(Integer.parseInt(userIds[1]));
        if (firstUser == null || secondUser == null){
            LOGGER.error("Users were not found by ids: "+ Arrays.toString(userIds));
            return null;
        }
        return new Room(roomId, firstUser.getUsername(), secondUser.getUsername());
    }

    @GetMapping(value = "/messages/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId, @RequestParam int offset, @RequestParam int size) {
        boolean roomExists = roomRepository.isRoomExists(roomId);
        List<Message> messages = new ArrayList<>();
        if (roomExists) {
            Set<String> values = roomRepository.getMessages(roomId, offset, size);
            for (String value : values) {
                messages.add(deserialize(value));
            }
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
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
