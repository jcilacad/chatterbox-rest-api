package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.request.UserRequest;
import com.projects.chatterboxapi.dto.response.TokenResponse;
import com.projects.chatterboxapi.dto.response.UrlResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AuthService {

    UserRequest getAuthenticatedUser();

    UrlResponse auth();

    TokenResponse callback(String code) throws IOException;
}
