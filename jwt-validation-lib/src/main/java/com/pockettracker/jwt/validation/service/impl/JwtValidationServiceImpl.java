package com.pockettracker.jwt.validation.service.impl;

import com.pockettracker.feign.user.AuthFeignClient;
import com.pockettracker.feign.user.dto.JwtRefreshTokenPair;
import com.pockettracker.jwt.properties.JwtProperties;
import com.pockettracker.jwt.util.JwtConstants;
import com.pockettracker.jwt.validation.service.JwtValidationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service("jwtService")
public class JwtValidationServiceImpl implements JwtValidationService {

    @Value("${app.jwt.public.key}")
    private String publicKey;

    private JwtProperties jwtProperties;

    private AuthFeignClient authFeignClient;

    public JwtValidationServiceImpl(JwtProperties jwtProperties, AuthFeignClient authFeignClient) {
        this.jwtProperties = jwtProperties;
        this.authFeignClient = authFeignClient;
    }

    @Override
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    @Override
    public boolean isTokenValid(String token, String refreshToken) {
        boolean tokenValid = isTokenValid(token);
        if (!tokenValid) {
            tokenValid = isTokenValid(refreshToken(token, refreshToken));
        }
        return tokenValid;

    }

    private String refreshToken(String token, String refreshToken) {
        JwtRefreshTokenPair refreshRequest = JwtRefreshTokenPair.builder()
                .authToken(token)
                .refreshToken(refreshToken)
                .build();
        JwtRefreshTokenPair newJwtRtPair = authFeignClient.refresh(refreshRequest);
        return newJwtRtPair.authToken();
    }

    @Override
    public Claims extractAllClaims(String jwtToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(retrievePublicKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    @Override
    public Long extractUserId(String token) {
        return extractClaim(token, claim -> claim.get(jwtProperties.getClaims().getId(), Long.class));
    }

    @Override
    public Collection<GrantedAuthority> extractAuthorities(String token) {
        String role = extractClaim(token, claims -> claims.get(jwtProperties.getClaims().getRole(), String.class));
        return List.of(new SimpleGrantedAuthority(role));
    }

    private Key retrievePublicKey() {
        try {
            PEMParser pemParser = new PEMParser(new StringReader(publicKey));
            PemObject pemObject = pemParser.readPemObject();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
            KeyFactory keyFactory = KeyFactory.getInstance(jwtProperties.getAlgorithm().getKeyAlgorithm());
            return keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Unexpected error when parsing public key");
            throw new IllegalArgumentException();
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public JwtProperties getJwtProperties() {
        return jwtProperties;
    }

    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }
}
