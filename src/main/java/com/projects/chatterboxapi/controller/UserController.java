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

import static com.projects.chatterboxapi.constants.OpenApiConstants.*;
import static com.projects.chatterboxapi.constants.PathConstants.API_V1_USERS;

@Tag(name = USER_CONTROLLER_TAG)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@RestController
@AllArgsConstructor
@RequestMapping(API_V1_USERS)
public class UserController {

    private final UserService userService;

    @Operation(summary = USERS_EXCEPT_AUTHENTICATED_USER)
    @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_DESCRIPTION)
    @GetMapping
    public ResponseEntity<List<UserRequest>> getUsersExceptAuthenticatedUser(@RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(userService.getUsersExceptAuthenticatedUser(name));
    }
}
