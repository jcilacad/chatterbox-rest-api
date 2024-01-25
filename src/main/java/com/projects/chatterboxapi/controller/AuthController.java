package com.projects.chatterboxapi.controller;

import com.google.gson.Gson;
import com.projects.chatterboxapi.dto.LoginData;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.utils.AppConstants;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Modify this, use oauth instead.
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginData loginData, HttpSession session) {
        String username = loginData.getUsername();

        boolean userExists = userRepository.isUserExists(username);
        if (!userExists) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userRepository.getUserByName(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.setOnline(true);

        Gson gson = new Gson();
        session.setAttribute(AppConstants.USER_ATTR_NAME, gson.toJson(user));
        LOGGER.info("Sign in user: {}", user.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        Object user = session.getAttribute(AppConstants.USER_ATTR_NAME);
        LOGGER.info("Sign out user: {}", user.toString());

        session.removeAttribute(AppConstants.USER_ATTR_NAME);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
