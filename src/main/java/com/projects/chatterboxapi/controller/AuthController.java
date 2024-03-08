package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.dto.response.TokenResponse;
import com.projects.chatterboxapi.dto.response.UrlResponse;
import com.projects.chatterboxapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.projects.chatterboxapi.constants.OpenApiConstants.*;
import static com.projects.chatterboxapi.constants.PathConstants.*;

@Tag(name = AUTH_CONTROLLER_TAG)
@RestController
@AllArgsConstructor
@RequestMapping(API_V1_AUTH)
public class AuthController {

    private final AuthService authService;

    @Operation(summary = AUTHENTICATED_USER)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @GetMapping(ME)
    public ResponseEntity<UserRequest> getAuthenticatedUser() {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

    @Operation(summary = AUTHENTICATION_URL)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @GetMapping(URL)
    public ResponseEntity<UrlResponse> auth() {
        return ResponseEntity.ok(authService.auth());
    }

    @Operation(summary = CALLBACK_URL)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @GetMapping(CALLBACK)
    public ResponseEntity<TokenResponse> callback(@RequestParam("code") String code) throws IOException {
        return ResponseEntity.ok(authService.callback(code));
    }

    @Operation(summary = LOGOUT_AUTHENTICATED_USER)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @SecurityRequirement(name = BEARER_AUTHENTICATION)
    @PostMapping(LOGOUT)
    public void logout(HttpServletResponse response) throws IOException {
        authService.processLogout(response);
    }
}
