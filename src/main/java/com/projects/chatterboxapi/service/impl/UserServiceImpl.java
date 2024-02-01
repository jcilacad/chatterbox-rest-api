package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.request.UserDtoRequest;
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
    public UserDtoRequest saveUser(UserDtoRequest userDtoRequest) {
        User user = UserMapper.MAPPER.toEntity(userDtoRequest);
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
    public UserDtoRequest fromGoogleUser(DefaultOidcUser googleUser) {
        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setId(googleUser.getSubject());
        userDtoRequest.setName(googleUser.getName());
        userDtoRequest.setEmail(googleUser.getEmail());
        userDtoRequest.setImageUrl(googleUser.getPicture());
        userDtoRequest.setActive(true);
        userDtoRequest.setDateCreated(googleUser.getIssuedAt());
        userDtoRequest.setDateUpdated(googleUser.getIssuedAt());
        return userDtoRequest;
    }

    @Override
    public List<UserDtoRequest> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDtoRequest currentUser = (UserDtoRequest) authentication.getPrincipal();
        String email = currentUser.getEmail();
        List<User> users = userRepository.findAll();
        List<UserDtoRequest> userDtoRequests = users.stream()
                .filter(user -> !user.getEmail().equalsIgnoreCase(email))
                .map(user -> UserMapper.MAPPER.toDto(user))
                .collect(Collectors.toList());
        return userDtoRequests;
    }

    @Override
    public String getJwtSecret() {
        return this.jwtSecret;
    }
}
