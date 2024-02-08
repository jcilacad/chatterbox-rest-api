package com.projects.chatterboxapi.security;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessEventListener {

    private final UserService userService;

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent successEvent) {
        Authentication authentication = successEvent.getAuthentication();

        if (authentication.getPrincipal() instanceof OAuth2AuthenticatedPrincipal) {
            OAuth2AuthenticatedPrincipal oidcUser = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            UserRequest userRequest = userService.fromGoogleUser(oidcUser);

            if (!userService.existsByEmail(userRequest.getEmail())) userService.saveUser(userRequest);
        }
    }
}