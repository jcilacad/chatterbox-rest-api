package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.exception.ResourceNotFoundException;
import com.projects.chatterboxapi.mapper.UserMapper;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.AuthService;
import com.projects.chatterboxapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public UserRequest saveUser(UserRequest userRequest) {
        User user = UserMapper.MAPPER.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return UserMapper.MAPPER.toDto(savedUser);
    }

    @Override
    @Transactional
    public void setActiveStatus(String email, boolean activeStatus) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        user.setActive(activeStatus);
        userRepository.save(user);
    }

    @Override
    public UserRequest fromGoogleUser(OAuth2AuthenticatedPrincipal googleUser) {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(googleUser.getAttribute("sub"));
        userRequest.setName(googleUser.getAttribute("name"));
        userRequest.setEmail(googleUser.getAttribute("email"));
        userRequest.setImageUrl(googleUser.getAttribute("picture"));
        userRequest.setActive(true);
        userRequest.setDateCreated(Instant.now());
        return userRequest;
    }

    @Override
    public List<UserRequest> getUsersExceptAuthenticatedUser(String name) {
        return name == null || name.trim().isEmpty() ? this.getUsersExcludingLoggedInUser() : this.getUsersByQueryName(name);
    }

    @Override
    public UserRequest findById(String id) {
        return userRepository.findById(id)
                .map(user -> UserMapper.MAPPER.toDto(user))
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
    }

    @Override
    public List<UserRequest> getUsersExcludingLoggedInUser() {
        UserRequest currentUser = authService.getAuthenticatedUser();
        String email = currentUser.getEmail();
        List<User> users = userRepository.findAllByEmailNotContaining(email);
        List<UserRequest> userRequests = users.stream()
                .map(user -> UserMapper.MAPPER.toDto(user))
                .collect(Collectors.toList());
        return userRequests;
    }

    @Override
    public List<UserRequest> getUsersByQueryName(String name) {
        UserRequest currentUser = authService.getAuthenticatedUser();
        String email = currentUser.getEmail();
        return userRepository.findByNameContainingAndEmailNotContainingAllIgnoreCase(name, email)
                .stream()
                .map(user -> UserMapper.MAPPER.toDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
