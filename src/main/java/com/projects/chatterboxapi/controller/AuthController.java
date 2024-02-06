package com.projects.chatterboxapi.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.dto.response.TokenResponse;
import com.projects.chatterboxapi.dto.response.UrlResponse;
import com.projects.chatterboxapi.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

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
