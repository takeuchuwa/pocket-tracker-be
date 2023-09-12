package com.pockettracker.auth.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.pockettracker.jwt")
@Getter
@Setter
public class JwtProperties {

    private String issuer = "admin@pockettracker.com";

    private Long expirationInMilliseconds = 1440000L;

    private final Claims claims;

    private final Algorithm algorithm;

    public JwtProperties() {
        this.claims = new Claims();
        this.algorithm = new Algorithm();
    }

    @Getter
    @Setter
    public static class Claims {
        private String id = "id";

        private String role = "role";
    }

    @Getter
    @Setter
    public static class Algorithm {

        private String signatureAlgorithm = "RS256";

        private String keyAlgorithm = "RSA";
    }


}
