package com.projects.chatterboxapi.mapper;

import com.projects.chatterboxapi.dto.request.UserDtoRequest;
import com.projects.chatterboxapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDtoRequest toDto(User user);

    User toEntity(UserDtoRequest userDtoRequest);
}
