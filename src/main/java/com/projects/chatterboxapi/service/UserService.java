package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.request.UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.List;

public interface UserService {

    UserRequest saveUser(UserRequest userRequest);

    void setActiveStatus(String email, boolean activeStatus);

    UserRequest fromGoogleUser(OAuth2AuthenticatedPrincipal oidcUser);

    List<UserRequest> getUsersExceptAuthenticatedUser(String name);

    UserRequest findById(String id);

    List<UserRequest> getUsersByQueryName(String name);

    List<UserRequest> getUsersExcludingLoggedInUser();
}
