package com.pockettracker.user.security.jwt.config;

import com.pockettracker.jwt.validation.filter.JwtAuthenticationFilter;
import com.pockettracker.user.security.jwt.service.JwtService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

    @Value("${app.jwt.sign.key}")
    private String jwtSignKey;

    @Value("${app.jwt.public.key}")
    private String jwtPublicKey;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }
}
