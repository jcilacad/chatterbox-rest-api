package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserRequest> google() {
        return ResponseEntity.ok(authService.user());
    }
}
