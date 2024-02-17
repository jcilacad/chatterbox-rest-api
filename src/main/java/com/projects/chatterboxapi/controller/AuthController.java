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

import static com.projects.chatterboxapi.constants.PathConstants.*;

@Tag(name = "RESTful APIs for Authentication Resource")
@RestController
@AllArgsConstructor
@RequestMapping(API_V1_AUTH)
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Get Authenticated User")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 Success")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(ME)
    public ResponseEntity<UserRequest> getAuthenticatedUser() {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

    @Operation(summary = "Get Authentication URL")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping(URL)
    public ResponseEntity<UrlResponse> auth() {
        return ResponseEntity.ok(authService.auth());
    }

    @Operation(summary = "Get Callback URL")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping(CALLBACK)
    public ResponseEntity<TokenResponse> callback(@RequestParam("code") String code) throws IOException {
        return ResponseEntity.ok(authService.callback(code));
    }

    @Operation(summary = "Logout Authenticated User")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(LOGOUT)
    public void logout(HttpServletResponse response) throws IOException {
        authService.processLogout(response);
    }
}
