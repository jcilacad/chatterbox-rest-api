package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "RESTful APIs For User Resource")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get Users Except Authenticated User")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @GetMapping
    public ResponseEntity<List<UserRequest>> getUsersExceptAuthenticatedUser(@RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(userService.getUsersExceptAuthenticatedUser(name));
    }
}
