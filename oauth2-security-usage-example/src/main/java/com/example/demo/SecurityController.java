package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
    @GetMapping
    public String greeting() {
        return "Welcome!";
    }
    @GetMapping("/health")
    public String health() {
        return "Up and Running!";
    }

    @GetMapping("/protected")
    @PreAuthorize("hasAnyAuthority('SCOPE_fakebook.read','ROLE_ADMIN')")
    public String admin() {
        return "Admin Page";
    }
}
