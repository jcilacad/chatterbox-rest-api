package com.projects.chatterboxapi.controller;

import com.projects.chatterboxapi.dto.response.MessageDto;
import com.projects.chatterboxapi.dto.response.TokenResponse;
import com.projects.chatterboxapi.dto.response.UrlResponse;
import com.projects.chatterboxapi.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
@Slf4j
public class TestController {

    private final AuthService authService;

    @GetMapping("/auth/url")
    public ResponseEntity<UrlResponse> auth() {
        return ResponseEntity.ok(authService.auth());
    }

    @GetMapping("/auth/callback")
    public ResponseEntity<TokenResponse> callback(@RequestParam("code") String code) throws URISyntaxException, IOException {
        return ResponseEntity.ok(authService.callback(code));
    }

    @GetMapping("/messages")
    public ResponseEntity<MessageDto> privateMessages() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticatedPrincipal oidcUser = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();

        String picture = oidcUser.getAttribute("picture");
        String myName = oidcUser.getName();
        String email = oidcUser.getAttribute("email");
        String subject = oidcUser.getAttribute("subject");
        String locale = oidcUser.getAttribute("locale");

        log.info("Picture - " + picture);
        log.info("Subject - " + subject);
        log.info("Name - " + myName);
        log.info("Email - " + email);
        log.info("Locale - " + locale);

        return ResponseEntity.ok(new MessageDto("Your name is " + myName));
    }

    @GetMapping("/public/messages")
    public ResponseEntity<MessageDto> publicMessages() {
        return ResponseEntity.ok(new MessageDto("public content"));
    }
}
