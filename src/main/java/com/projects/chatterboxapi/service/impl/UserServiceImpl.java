package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.UserDto;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.exception.ResourceNotFoundException;
import com.projects.chatterboxapi.mapper.UserMapper;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.MAPPER.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.MAPPER.toDto(savedUser);
    }

    @Override
    public void setActiveStatus(String email, boolean activeStatus) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        user.setActive(activeStatus);
    }

    @Override
    public String getJwtSecret() {
        return this.jwtSecret;
    }
}
