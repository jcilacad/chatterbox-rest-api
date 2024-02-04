package com.projects.chatterboxapi.service.impl;

import com.projects.chatterboxapi.dto.request.UserRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String jwtSecret;

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
    public UserRequest fromGoogleUser(DefaultOidcUser googleUser) {
        UserRequest userRequest = new UserRequest();
        userRequest.setId(googleUser.getSubject());
        userRequest.setName(googleUser.getFullName());
        userRequest.setEmail(googleUser.getEmail());
        userRequest.setImageUrl(googleUser.getPicture());
        userRequest.setActive(true);
        userRequest.setDateCreated(googleUser.getIssuedAt());
        userRequest.setDateUpdated(googleUser.getIssuedAt());
        return userRequest;
    }

    @Override
    public List<UserRequest> getUsers() {
        UserRequest currentUser = this.getLoggedInUser();
        String email = currentUser.getEmail();
        List<User> users = userRepository.findAll();
        List<UserRequest> userRequests = users.stream()
                .filter(user -> !user.getEmail().equalsIgnoreCase(email))
                .map(user -> UserMapper.MAPPER.toDto(user))
                .collect(Collectors.toList());
        return userRequests;
    }

    @Override
    public List<UserRequest> getUsersByName(String name) {
        List<UserRequest> filteredUsers = this.getUsers();
        return filteredUsers.stream()
                .filter(ur -> ur.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    @Override
    public UserRequest getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserRequest) authentication.getPrincipal();
    }

    @Override
    public UserRequest findById(String id) {
        return userRepository.findById(id)
                .map(user -> UserMapper.MAPPER.toDto(user))
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
    }

    @Override
    public String getJwtSecret() {
        return this.jwtSecret;
    }
}
