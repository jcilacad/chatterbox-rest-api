package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.request.UserRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.List;

public interface UserService {

    UserRequest saveUser(UserRequest userRequest);

    String getJwtSecret();

    void setActiveStatus(String email, boolean activeStatus);

    UserRequest fromGoogleUser(DefaultOidcUser oidcUser);

    List<UserRequest> getUsers();

    List<UserRequest> getUsersByName(String name);

    UserRequest getLoggedInUser();

    UserRequest findById(String id);
}
