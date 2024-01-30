package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.UserDto;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.exception.ResourceNotFoundException;
import com.projects.chatterboxapi.mapper.UserMapper;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
        userRepository.save(user);
    }

    @Override
    public UserDto fromGoogleUser(DefaultOidcUser googleUser) {
        UserDto userDto = new UserDto();
        userDto.setId(googleUser.getSubject());
        userDto.setName(googleUser.getName());
        userDto.setEmail(googleUser.getEmail());
        userDto.setImageUrl(googleUser.getPicture());
        userDto.setActive(true);
        userDto.setDateCreated(googleUser.getIssuedAt());
        userDto.setDateUpdated(googleUser.getIssuedAt());
        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = (UserDto) authentication.getPrincipal();
        String email = currentUser.getEmail();
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .filter(user -> !user.getEmail().equalsIgnoreCase(email))
                .map(user -> UserMapper.MAPPER.toDto(user))
                .collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public String getJwtSecret() {
        return this.jwtSecret;
    }
}
