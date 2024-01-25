package com.projects.chatterboxapi.controller;

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
    private RoomService roomService;

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Room>> getRooms(@PathVariable int userId) {
       return ResponseEntity.ok(roomService.getRooms(userId));
    }

    @GetMapping(value = "/messages/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId,
                                                     @RequestParam int offset,
                                                     @RequestParam int size) {
        return ResponseEntity.ok(roomService.getMessages(roomId, offset, size));
    }
}
