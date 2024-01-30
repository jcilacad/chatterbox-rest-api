package com.projects.chatterboxapi.dto;

import lombok.*;

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
}
