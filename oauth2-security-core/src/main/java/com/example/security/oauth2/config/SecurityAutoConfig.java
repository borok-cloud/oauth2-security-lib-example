package com.example.security.oauth2.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
//@ConditionalOnProperty(prefix = "security.oauth2", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityAutoConfig {

    @Value("${okta.issuer-uri}") String issuerUri;

    @Value("${security.session-policy:IF_REQUIRED}")
    SessionCreationPolicy sessionPolicy;

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        //  .requestMatchers("/books/**").hasAuthority("SCOPE_fakebook.read")
                        //.requestMatchers("/books").hasAuthority("SCOPE_fakebook.admin")
                        .requestMatchers("/health",
                                //"/protected",
                                "/actuator/**","/").permitAll()

                        .anyRequest().authenticated()
                )
                // Register the request cache at the root level
                .requestCache(requestCache ->
                        requestCache.requestCache(new HttpSessionRequestCache())
                )

                .oauth2Login(oauth2 -> oauth2
                        //to strip query params /?continue from redirect URL appended
                        // by okta after login success oidcSuccessHandler()
                        .successHandler(oidcSuccessHandler())
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(new CustomOidcUserService(jwtDecoder()))
                        )
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter())))
                .sessionManagement(s -> s.sessionCreationPolicy(sessionPolicy));
        return http.build();
    }



    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        //converter.setJwtGrantedAuthoritiesConverter(extractor::extractAuthorities);
        return converter;
    }

    //@Bean
    public JwtDecoder jwtDecoder() {
        JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
        return jwtDecoder;
    }

    @Bean
    public AuthenticationSuccessHandler oidcSuccessHandler() {
        return (request, response, authentication) -> {
            // Remove ?continue param if present
            String targetUrl = ((SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST")) != null
                    ? ((SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST")).getRedirectUrl()
                    : "/";

            // Clean up the session
            request.getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");

            // Do a clean redirect
            response.sendRedirect(targetUrl.split("\\?")[0]); // strips query params
        };
    }




}
