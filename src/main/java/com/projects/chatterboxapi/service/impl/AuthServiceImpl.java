package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.request.UserDtoRequest;
import com.projects.chatterboxapi.service.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public UserDtoRequest user() {
        return (UserDtoRequest) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
