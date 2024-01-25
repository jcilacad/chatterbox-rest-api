package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.Message;
import com.projects.chatterboxapi.entity.Room;
import com.projects.chatterboxapi.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
