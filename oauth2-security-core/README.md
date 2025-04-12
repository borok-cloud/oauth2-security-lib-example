# Core Security Shared Library

This library provides a reusable Spring Boot auto-configuration for securing downstream applications using OAuth2 Resource Server configuration with customizable CORS and authorization rules.

## Features

- Declarative YAML-based configuration
- Support for `permitAll` URL patterns
- Role-based authority rules
- CORS configuration
- Plug-and-play via Spring Boot autoconfiguration

---

## How to Use

### 1. Add the Shared Security Library Dependency

If published to a Maven repository, add it to your downstream Spring Boot application:

```xml
<dependency>
  <groupId>com.example.security</groupId>
  <artifactId>core-security-shared</artifactId>
  <version>1.0.0</version>
</dependency>
```

Or include the shared module as a project dependency if local:

```groovy
implementation project(':core-security-shared')
```

---

### 2. Enable Configuration Properties

Make sure Spring Boot picks up the configuration properties by using `@EnableConfigurationProperties`:

Already included in the shared library:
```java
@EnableConfigurationProperties(SecurityRuleProperties.class)
```

No need to repeat this in the downstream app.

---

### 3. Use YAML to Configure Security Rules

In the downstream app's `application.yml`, configure your rules:

```yaml
server:
  port: 8080
  servlet:
    context-path: /myapp

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://dev-xxxx.okta.com/oauth2/default/v1/keys

security:
  oauth2:
    enabled: true
    permit-all:
      - /
      - /health
      - /public/**
    authority-rules:
      /api/secure/**:
        - ROLE_USER
    allowed-origins:
      - https://myfrontend.com
      - https://admin.myapp.io
```

---

### 4. Secure Your Controllers

All endpoints will be authenticated unless specified in `permit-all`. Use roles for protected endpoints:

```java
@GetMapping("/api/secure/data")
@PreAuthorize("hasAuthority('ROLE_USER')")
public ResponseEntity<String> getData() {
    return ResponseEntity.ok("Secure data");
}
```

---

### 5. CORS Support

CORS is automatically configured based on the `allowed-origins` list in your YAML file.

---

### 6. Auto-configuration Activation

Ensure this file exists in your shared library:

**File:** `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`

```
com.example.security.config.SecurityRulesAutoConfig
```

This lets Spring Boot auto-load your security configuration.

---

## License

MIT