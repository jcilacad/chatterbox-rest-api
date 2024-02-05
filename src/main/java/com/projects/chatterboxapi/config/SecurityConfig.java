package com.projects.chatterboxapi.config;

import com.projects.chatterboxapi.security.CustomOidcLogoutSuccessHandler;
import com.projects.chatterboxapi.security.GoogleOpaqueTokenIntrospector;
import com.projects.chatterboxapi.security.Oauth2AuthenticationEntryPoint;
import com.projects.chatterboxapi.service.UserService;
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

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final WebClient userInfoClient;
    private final UserService userService;

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
                .logout(logout -> logout
                        .logoutSuccessHandler(new CustomOidcLogoutSuccessHandler(userService))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/auth/**", "/public/**").permitAll()
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
