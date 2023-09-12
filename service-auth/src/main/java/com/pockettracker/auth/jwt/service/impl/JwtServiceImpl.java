package com.pockettracker.auth.jwt.service.impl;


import com.pockettracker.auth.jwt.config.JwtConfig;
import com.pockettracker.auth.jwt.service.JwtService;
import com.pockettracker.auth.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public record JwtServiceImpl(JwtConfig jwtConfig) implements JwtService {

    @Override
    public String generateToken(User user) {
        Map<String, Object> extraClaims = populateClaims(user);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(UUID.randomUUID().toString())
                .setIssuer(jwtConfig.getJwtProperties().getIssuer())
                .setSubject(user.getUserCredentials().getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getJwtProperties().getExpirationInMilliseconds()))
                .signWith(getSigningKey(), SignatureAlgorithm.forName(jwtConfig.getJwtProperties().getAlgorithm().getSignatureAlgorithm()))
                .compact();
    }

    private Map<String, Object> populateClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(jwtConfig.getJwtProperties().getClaims().getId(),user.getUserId());
        extraClaims.put(jwtConfig.getJwtProperties().getClaims().getRole(), user.getRole());
        return extraClaims;
    }


    private Key getSigningKey() {
        String key = jwtConfig.getJwtSignKey();
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(key));
            KeyFactory keyFactory = KeyFactory.getInstance(jwtConfig.getJwtProperties().getAlgorithm().getKeyAlgorithm());
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Unexpected error when parsing private key");
            throw new IllegalArgumentException();
        }
    }
}
