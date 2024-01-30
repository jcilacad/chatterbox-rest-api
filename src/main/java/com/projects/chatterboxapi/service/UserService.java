package com.projects.chatterboxapi.service;

import com.projects.chatterboxapi.dto.UserDto;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    String getJwtSecret();

    void setActiveStatus(String email, boolean activeStatus);

    UserDto fromGoogleUser(DefaultOidcUser oidcUser);

    List<UserDto> getUsers();
}
