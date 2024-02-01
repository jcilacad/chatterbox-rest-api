package com.projects.chatterboxapi.security;

import com.projects.chatterboxapi.dto.request.UserDtoRequest;
import com.projects.chatterboxapi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Authentication: {}", authentication);
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        String idToken = oidcUser.getIdToken().getTokenValue();
        String jwt = Jwts.builder()
                .setSubject(idToken)
                .setIssuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(userService.getJwtSecret().getBytes()))
                .compact();
        // TODO: Send the jwt to the client for authentication and authorization.
        UserDtoRequest userDtoRequest = userService.fromGoogleUser(oidcUser);
        UserDtoRequest savedUser = userService.saveUser(userDtoRequest);
        AppAuthenticationToken token = new AppAuthenticationToken(savedUser);
        SecurityContextHolder.getContext().setAuthentication(token);
        // TODO: Create the constant value, coming from the env.properties
        response.sendRedirect("http://localhost:8080/api/v1/users");
    }
}
