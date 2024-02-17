package com.projects.chatterboxapi.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.dto.response.TokenResponse;
import com.projects.chatterboxapi.dto.response.UrlResponse;
import com.projects.chatterboxapi.entity.User;
import com.projects.chatterboxapi.exception.ResourceNotFoundException;
import com.projects.chatterboxapi.mapper.UserMapper;
import com.projects.chatterboxapi.repository.UserRepository;
import com.projects.chatterboxapi.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Value("${front.end.url}")
    private String frontendUrl;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientSecret}")
    private String clientSecret;

    @Override
    public UserRequest getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticatedPrincipal oidcUser = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
        String subject = oidcUser.getAttribute("sub");
        User user = userRepository.findById(subject)
                .orElseThrow(() -> new ResourceNotFoundException("User" , "id", subject));
        return UserMapper.MAPPER.toDto(user);
    }

    @Override
    public UrlResponse auth() {
        String url = new GoogleAuthorizationCodeRequestUrl(clientId, frontendUrl,
                Arrays.asList("email", "profile", "openid")).build();
        return new UrlResponse(url);
    }

    @Override
    public TokenResponse callback(String code) throws IOException {
        String token;
        token = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(), new GsonFactory(),
                clientId,
                clientSecret,
                code,
                frontendUrl
        ).execute().getAccessToken();
        return new TokenResponse(token);
    }

    @Override
    public void processLogout(HttpServletResponse httpServletResponse) throws IOException {
        User user = UserMapper.MAPPER.toEntity(this.getAuthenticatedUser());
        user.setActive(false);
        userRepository.save(user);
        httpServletResponse.sendRedirect("https://accounts.google.com/logout");
    }
}
