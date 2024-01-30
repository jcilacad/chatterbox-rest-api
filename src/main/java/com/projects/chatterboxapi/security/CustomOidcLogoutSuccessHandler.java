package com.projects.chatterboxapi.security;

import com.projects.chatterboxapi.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOidcLogoutSuccessHandler extends OidcClientInitiatedLogoutSuccessHandler {

    private final UserService userService;

    @Autowired
    public CustomOidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository,
                                          UserService userService) {
        super(clientRegistrationRepository);
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            String email = oidcUser.getEmail();
            userService.setActiveStatus(email, false);
        }
        super.onLogoutSuccess(request, response, authentication);
    }
}
