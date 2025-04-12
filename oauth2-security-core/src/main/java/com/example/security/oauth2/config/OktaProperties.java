package com.example.security.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "okta")
public record OktaProperties(
        String jwkSetUri,
        String clientId,
        String clientSecret,
        String scope,
        String authorizationUri,
        String tokenUri,
        String userInfoUri,
        String userNameAttribute,
        String redirectUri,
        String authorizationGrantType,
        String healthCheckUri
) {}

