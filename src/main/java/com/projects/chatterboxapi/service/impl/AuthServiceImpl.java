package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.service.AuthService;
import com.projects.chatterboxapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserRequest user() {
        return userService.getLoggedInUser();
    }
}
