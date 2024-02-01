package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.request.UserDtoRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.List;

public interface UserService {

    UserDtoRequest saveUser(UserDtoRequest userDtoRequest);

    String getJwtSecret();

    void setActiveStatus(String email, boolean activeStatus);

    UserDtoRequest fromGoogleUser(DefaultOidcUser oidcUser);

    List<UserDtoRequest> getUsers();
}
