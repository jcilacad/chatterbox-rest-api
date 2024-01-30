package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    String getJwtSecret();

    void setActiveStatus(String email, boolean activeStatus);

    List<UserDto> getUsers();
}
