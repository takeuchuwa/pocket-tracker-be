package com.pockettracker.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.pockettracker.jwt")
@Getter
@Setter
public class JwtProperties {

    private String issuer = "admin@pockettracker.com";

    private Long expirationInMilliseconds = 1000 * 60 * 60 * 24L; // 1 day default

    private Long refreshExpirationInMilliseconds = 1000 * 60 * 60 * 24 * 7L; // 7 day default

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