package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, User>> get(@RequestParam(value = "ids") String idsString) {
        return ResponseEntity.ok(userService.get(idsString));
    }

    @RequestMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getMe(HttpSession session) {
        return ResponseEntity.ok(userService.getMe(session));
    }

    @RequestMapping(value = "/online", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, User>> getOnline() {
        return ResponseEntity.ok(userService.getOnline());
    }
}
