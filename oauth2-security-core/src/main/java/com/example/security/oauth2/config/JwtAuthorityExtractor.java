package com.example.security.oauth2.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;

@FunctionalInterface
public interface JwtAuthorityExtractor {
    Collection<GrantedAuthority> extractAuthorities(Jwt jwt);
}
