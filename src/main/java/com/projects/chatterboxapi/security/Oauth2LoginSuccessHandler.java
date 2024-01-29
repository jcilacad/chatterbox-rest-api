package com.projects.chatterboxapi.security;

import com.projects.chatterboxapi.dto.UserDto;
import com.projects.chatterboxapi.service.UserService;
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

@Slf4j
@AllArgsConstructor
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Authentication: {}", authentication);
        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        UserDto userDto = UserDto.fromGoogleUser(oidcUser);
        UserDto savedUser = userService.saveUser(userDto);
        AppAuthenticationToken token = new AppAuthenticationToken(savedUser);
        SecurityContextHolder.getContext().setAuthentication(token);
        response.sendRedirect("http://localhost:8080/login-success");
    }
}
