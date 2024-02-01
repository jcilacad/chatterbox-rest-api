package com.projects.chatterboxapi.mapper;

import com.projects.chatterboxapi.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMessageMapper {

    ChatMessageMapper MAPPER = Mappers.getMapper(ChatMessageMapper.class);

    com.projects.chatterboxapi.dto.response.ChatMessageResponse toDto(ChatMessage chatMessage);

    ChatMessage toEntity(com.projects.chatterboxapi.dto.response.ChatMessageResponse chatMessageResponse);
}
