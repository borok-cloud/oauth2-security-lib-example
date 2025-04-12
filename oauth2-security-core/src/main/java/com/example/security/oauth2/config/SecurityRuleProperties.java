package com.example.security.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "security.oauth2")
public class SecurityRuleProperties {

    private List<String> permitAll = new ArrayList<>();
    private Map<String, List<String>> authorityRules = new HashMap<>();
    private List<String> allowedOrigins = new ArrayList<>();

    public List<String> getPermitAll() {
        return permitAll;
    }

    public void setPermitAll(List<String> permitAll) {
        this.permitAll = permitAll;
    }

    public Map<String, List<String>> getAuthorityRules() {
        return authorityRules;
    }

    public void setAuthorityRules(Map<String, List<String>> authorityRules) {
        this.authorityRules = authorityRules;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
