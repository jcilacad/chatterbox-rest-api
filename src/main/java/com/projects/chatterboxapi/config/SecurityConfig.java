package com.projects.chatterboxapi.config;

import com.projects.chatterboxapi.security.GoogleOpaqueTokenIntrospector;
import com.projects.chatterboxapi.security.Oauth2AuthenticationEntryPoint;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.reactive.function.client.WebClient;

import static com.projects.chatterboxapi.constants.OpenApiConstants.*;
import static com.projects.chatterboxapi.constants.PathConstants.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@SecurityScheme(
        name = BEARER_AUTHENTICATION,
        type = SecuritySchemeType.HTTP,
        bearerFormat = JWT,
        scheme = BEARER
)
public class SecurityConfig {

    private final WebClient userInfoClient;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(c -> c
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint(
                            new Oauth2AuthenticationEntryPoint());
                })
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(BASE, PUBLIC_ALL).permitAll()
                        .requestMatchers(API_V1_AUTH_URL).permitAll()
                        .requestMatchers(API_V1_AUTH_CALLBACK).permitAll()
                        .requestMatchers(SWAGGER_UI_ALL).permitAll()
                        .requestMatchers(V3_API_DOCS_ALL).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(c -> c
                        .opaqueToken(Customizer.withDefaults())
                );
        return http.build();
    }

    @Bean
    public OpaqueTokenIntrospector introspector() {
        return new GoogleOpaqueTokenIntrospector(userInfoClient);
    }
}
