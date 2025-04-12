package com.example.security.oauth2.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnProperty(prefix = "security.oauth2", name = "enabled", havingValue = "false")
public class NoopSecurityConfig {

    @Bean
    public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable()); // Optional: disable CSRF
        return http.build();
    }
}