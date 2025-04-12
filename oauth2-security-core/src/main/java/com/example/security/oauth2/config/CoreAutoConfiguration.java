package com.example.security.oauth2.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({
        SecurityAutoConfig.class,
        NoopSecurityConfig.class,
        OktaConfig.class
       // JwtDecoderConfig.class  // (Optional: if you have other split config classes)
})
public class CoreAutoConfiguration {
    // No need to define beans here. It's just a glue class.
}
