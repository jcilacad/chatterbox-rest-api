package com.projects.chatterboxapi.mapper;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserRequest toDto(User user);

    User toEntity(UserRequest userRequest);
}
