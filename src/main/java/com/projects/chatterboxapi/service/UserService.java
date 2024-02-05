package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.request.UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.List;

public interface UserService {

    UserRequest saveUser(UserRequest userRequest);

    void setActiveStatus(String email, boolean activeStatus);

    UserRequest fromGoogleUser(OAuth2AuthenticatedPrincipal oidcUser);

    List<UserRequest> getUsers();

    List<UserRequest> getUsersByName(String name);

    UserRequest getLoggedInUser();

    UserRequest findById(String id);
}
