package com.pockettracker.user.security.jwt.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtPrivateConfig {

    @Value("${app.jwt.sign.key}")
    private String jwtSignKey;

}
