
# Custom Security Shared Library

This shared library provides a flexible and configurable Spring Security setup for downstream applications using JWT-based OAuth2 Resource Server security, CORS configuration, and authority-based access rules.

## Features

- Declarative security rule configuration via YAML or `.properties`
- Support for OAuth2 resource server (JWT validation)
- Configurable permit-all paths
- Configurable authority-based access control rules
- CORS configuration support

## How to Use

### 1. Add Dependency
Include the shared library as a dependency in your downstream Spring Boot application. For example:

```xml
<dependency>
    <groupId>com.example.security</groupId>
    <artifactId>core-security</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Enable Security via Configuration
To enable or disable the security configuration provided by the shared library, set the following property in your `application.yml` or `application.properties`:

```yaml
security:
  oauth2:
    enabled: true # Set to false to disable auto-configuration
```

### 3. Configure Security Rules
Example `application.yml`:

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
          jwk-set-uri: https://dev-58281825.okta.com/oauth2/default/v1/keys

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

### 4. CORS Configuration
Ensure CORS is configured in your downstream application:

```yaml
spring:
  web:
    cors:
      allowed-origins:
        - https://myfrontend.com
        - https://admin.myapp.io
      allowed-methods:
        - GET
        - POST
        - PUT
        - DELETE
        - OPTIONS
      allowed-headers:
        - "*"
      allow-credentials: true
```

### 5. Define Secure Endpoints
In your controllers, use `@PreAuthorize` or rely on the authority rules configured.

```java
@GetMapping("/api/secure/data")
@PreAuthorize("hasAuthority('ROLE_USER')")
public ResponseEntity<String> getSecureData() {
    return ResponseEntity.ok("Secure content");
}
```

---

Once configured, the downstream application will automatically pick up the security rules and CORS settings as per your application configuration.