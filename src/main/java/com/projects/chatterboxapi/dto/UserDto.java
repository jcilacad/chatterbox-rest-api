package com.projects.chatterboxapi.dto;

import com.projects.chatterboxapi.entity.User;
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
    private boolean active;
    private Instant dateCreated;
    private Instant dateUpdated;

    public static UserDto fromGoogleUser(DefaultOidcUser googleUser) {
        UserDto userDto = new UserDto();
        userDto.setId(googleUser.getSubject());
        userDto.setName(googleUser.getName());
        userDto.setEmail(googleUser.getEmail());
        userDto.setImageUrl(googleUser.getPicture());
        userDto.setActive(true);
        userDto.setDateCreated(googleUser.getIssuedAt());
        userDto.setDateUpdated(googleUser.getIssuedAt());
        return userDto;
    }
}
