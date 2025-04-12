package com.example.demo;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.List;

//@Configuration
//@EnableConfigurationProperties(OktaProperties.class)
public class OktaConfig {

    private final OktaProperties oktaProperties;

    public OktaConfig(OktaProperties oktaProperties) {
        this.oktaProperties = oktaProperties;
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration oktaClient = ClientRegistration.withRegistrationId("okta")
                .clientId(oktaProperties.clientId())
                .clientSecret(oktaProperties.clientSecret())
                .scope(oktaProperties.scope().split(","))
                .authorizationUri(oktaProperties.authorizationUri())
                .tokenUri(oktaProperties.tokenUri())
                .userInfoUri(oktaProperties.userInfoUri())
                .userNameAttributeName(oktaProperties.userNameAttribute())
                .redirectUri(oktaProperties.redirectUri())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .jwkSetUri(oktaProperties.jwkSetUri())
                .build();

        return new InMemoryClientRegistrationRepository(List.of(oktaClient));
    }

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri(oktaProperties.jwkSetUri()).build();
//    }
}

