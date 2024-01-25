package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.Message;
import com.projects.chatterboxapi.entity.Room;

import java.util.List;

public interface RoomService {
    List<Room> getRooms(int userId);
    List<Message> getMessages(String roomId, int offset, int size);
}
