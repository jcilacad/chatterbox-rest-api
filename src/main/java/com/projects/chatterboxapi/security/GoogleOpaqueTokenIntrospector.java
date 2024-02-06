package com.projects.chatterboxapi.security;

import com.projects.chatterboxapi.dto.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final WebClient userInfoClient;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        UserInfoResponse userInfoResponse = userInfoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(UserInfoResponse.class)
                .block();
        Map<String, Object> attributes = getStringObjectMap(userInfoResponse);
        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfoResponse.getName(), attributes, null);
    }

    private static Map<String, Object> getStringObjectMap(UserInfoResponse userInfoResponse) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userInfoResponse.getSub());
        attributes.put("name", userInfoResponse.getName());
        attributes.put("picture", userInfoResponse.getPicture());
        attributes.put("email", userInfoResponse.getEmail());
        attributes.put("familyName", userInfoResponse.getFamily_name());
        attributes.put("locale", userInfoResponse.getLocale());
        attributes.put("subject", userInfoResponse.getSub());
        attributes.put("givenName", userInfoResponse.getGiven_namep());
        attributes.put("emailVerified", userInfoResponse.isEmail_verified());
        return attributes;
    }
}