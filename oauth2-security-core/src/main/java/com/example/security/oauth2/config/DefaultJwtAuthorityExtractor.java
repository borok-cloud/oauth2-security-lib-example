package com.example.security.oauth2.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;


import java.util.*;

public class DefaultJwtAuthorityExtractor implements JwtAuthorityExtractor {
    @Override
    public Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Optional.ofNullable(jwt.getClaimAsString("client_id"))
            .ifPresent(clientId -> {
                switch (clientId) {
                    case "frontend-client-id" -> authorities.add(new SimpleGrantedAuthority("ROLE_FRONTEND"));
                    case "mobile-client-id" -> authorities.add(new SimpleGrantedAuthority("ROLE_MOBILE"));
                    default -> {}
                }
            });

        Optional.ofNullable(jwt.getClaim("scp")).ifPresent(scope -> {
            if (scope instanceof String s) {
                Arrays.stream(s.split(" "))
                      .map(str -> new SimpleGrantedAuthority("SCOPE_" + str.trim()))
                      .forEach(authorities::add);
            } else if (scope instanceof List<?> list) {
                list.stream()
                    .map(Object::toString)
                    .map(str -> new SimpleGrantedAuthority("SCOPE_" + str))
                    .forEach(authorities::add);
            }
        });

        Optional.ofNullable(jwt.getClaimAsStringList("roles")).ifPresent(roles ->
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())))
        );

        return authorities;
    }
}
