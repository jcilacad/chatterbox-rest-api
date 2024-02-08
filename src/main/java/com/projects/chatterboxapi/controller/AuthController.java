package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.dto.response.TokenResponse;
import com.projects.chatterboxapi.dto.response.UrlResponse;
import com.projects.chatterboxapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserRequest> getAuthenticatedUser() {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

    @GetMapping("/url")
    public ResponseEntity<UrlResponse> auth() {
        return ResponseEntity.ok(authService.auth());
    }

    @GetMapping("/callback")
    public ResponseEntity<TokenResponse> callback(@RequestParam("code") String code) throws IOException {
        return ResponseEntity.ok(authService.callback(code));
    }
}
