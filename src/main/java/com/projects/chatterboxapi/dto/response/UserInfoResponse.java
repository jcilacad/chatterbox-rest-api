package com.projects.chatterboxapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private String sub;
    private String name;
    private String given_namep;
    private String family_name;
    private String picture;
    private String email;
    private boolean email_verified;
    private String locale;
}
