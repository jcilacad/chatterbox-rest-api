package com.projects.chatterboxapi.dto;

import lombok.*;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private Instant dateCreated;
    private Instant dateUpdated;

    public static UserDto fromGoogleUser(DefaultOidcUser googleUser) {
        UserDto userDto = new UserDto();
        userDto.id = googleUser.getSubject();
        userDto.name = googleUser.getFullName();
        userDto.email = googleUser.getEmail();
        userDto.imageUrl = googleUser.getPicture();
        return userDto;
    }
}
