package com.backend.auth_service.secret.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {

    private String secretKey;
    private int accessExpiresIn;
    private int refreshExpiresIn;
} 