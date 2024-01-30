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
    private boolean isActive;
    private Instant dateCreated;
    private Instant dateUpdated;

    public static UserDto fromGoogleUser(DefaultOidcUser googleUser) {
        UserDto userDto = UserDto.builder()
                .id(googleUser.getSubject())
                .name(googleUser.getFullName())
                .email(googleUser.getEmail())
                .imageUrl(googleUser.getPicture())
                .isActive(true)
                .dateCreated(googleUser.getIssuedAt())
                .dateUpdated(googleUser.getIssuedAt())
                .build();
        return userDto;
    }
}
