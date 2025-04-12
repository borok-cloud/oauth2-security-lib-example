
# Custom Security Shared Library

This shared library provides a flexible and configurable Spring Security setup for downstream applications using JWT-based OAuth2 Resource Server security, CORS configuration, and authority-based access rules.

## Features

- Declarative security rule configuration via YAML or `.properties`
- Support for OAuth2 resource server (JWT validation)
- Configurable permit-all paths
- Configurable authority-based access control rules
- CORS configuration support

## Auto-Configuration

The Core Security Shared Library is designed for **auto-configuration** in Spring Boot applications. By simply adding the dependency and providing configuration settings in your `application.yml` or `application.properties` file, the library will automatically configure essential security features for your application.

### Key Features of Auto-Configuration:

- **OAuth2 Resource Server**: Configures your application as an OAuth2 resource server, including automatic JWT validation based on the provided `jwk-set-uri`.
- **CORS Configuration**: Automatically applies CORS settings, including allowed origins, methods, and headers, based on your application's configuration.
- **Permit-All URL Patterns**: Automatically exposes URL patterns (like `/public/**`, `/health`, etc.) to be excluded from authentication, making it easy to define public endpoints.
- **Role-Based Authority Rules**: Automatically secures endpoints and enforces role-based access control using the `authority-rules` defined in the configuration file.

Once the dependency is added and the configuration is provided, the security features are automatically enabled without any manual intervention. This allows for easy and fast security setup with minimal code changes.

No custom configuration or additional setup is required, making it ideal for applications that need quick and standardized security settings.

---

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