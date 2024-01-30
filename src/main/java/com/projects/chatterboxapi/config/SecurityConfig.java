package com.projects.chatterboxapi.config;

import com.projects.chatterboxapi.security.CustomOidcLogoutSuccessHandler;
import com.projects.chatterboxapi.security.Oauth2AuthenticationEntryPoint;
import com.projects.chatterboxapi.security.Oauth2LoginSuccessHandler;
import com.projects.chatterboxapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint(
                            new Oauth2AuthenticationEntryPoint());
                })

                .logout(logout -> logout
                        .logoutSuccessHandler(customOidcLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                ).oauth2Login(customizer -> {
                    customizer
                            .successHandler(new Oauth2LoginSuccessHandler(userService));
                });
        return httpSecurity.build();
    }

    private CustomOidcLogoutSuccessHandler customOidcLogoutSuccessHandler() {
        CustomOidcLogoutSuccessHandler successHandler = new CustomOidcLogoutSuccessHandler(clientRegistrationRepository, userService);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080");
        return successHandler;
    }
}
