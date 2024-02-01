package com.projects.chatterboxapi.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private boolean active;
    private Instant dateCreated;
    private Instant dateUpdated;
}
