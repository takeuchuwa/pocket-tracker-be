package com.pockettracker.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@Getter
@AutoConfiguration
@ComponentScan(basePackages = "com.pockettracker.jwt")
public class JwtConfig {

    @Value("${app.jwt.public.key}")
    private String jwtPublicKey;
}
