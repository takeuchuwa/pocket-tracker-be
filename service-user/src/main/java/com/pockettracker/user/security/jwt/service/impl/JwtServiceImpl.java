package com.pockettracker.user.security.jwt.service.impl;


import com.pockettracker.jwt.properties.JwtProperties;
import com.pockettracker.jwt.validation.service.impl.JwtValidationServiceImpl;
import com.pockettracker.user.entity.User;
import com.pockettracker.user.security.jwt.config.JwtPrivateConfig;
import com.pockettracker.user.security.jwt.entity.JwtRtPair;
import com.pockettracker.user.security.jwt.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("extendedJwtService")
@Primary
@Slf4j
public class JwtServiceImpl extends JwtValidationServiceImpl implements JwtService {

    private final JwtPrivateConfig jwtConfig;
    private final RedisTemplate<String, JwtRtPair> redisTemplate;

    public JwtServiceImpl(JwtPrivateConfig jwtConfig, RedisTemplate<String, JwtRtPair> redisTemplate, JwtProperties jwtProperties) {
        super(jwtProperties);
        this.jwtConfig = jwtConfig;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> extraClaims = populateClaims(user);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(UUID.randomUUID().toString())
                .setIssuer(getJwtProperties().getIssuer())
                .setSubject(user.getUserCredentials().getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getJwtProperties().getExpirationInMilliseconds()))
                .signWith(getSigningKey(), SignatureAlgorithm.forName(getJwtProperties().getAlgorithm().getSignatureAlgorithm()))
                .compact();
    }

    @Override
    public String generateJwtRtPair(String jwtToken) {
        JwtRtPair jwtRtPair = JwtRtPair.builder()
                .jwtId(extractId(jwtToken))
                .refreshToken(UUID.randomUUID().toString())
                .build();
        String key = String.valueOf(extractUserId(jwtToken));
        redisTemplate.opsForValue().set(key, jwtRtPair);
        redisTemplate.expireAt(key, Instant.ofEpochMilli(System.currentTimeMillis() + getJwtProperties().getRefreshExpirationInMilliseconds()));
        return jwtRtPair.getRefreshToken();
    }

    @Override
    public boolean isJwtRtPairValid(String jwtToken, String refreshToken) {
        JwtRtPair rt = redisTemplate.opsForValue().getAndDelete(String.valueOf(extractUserId(jwtToken)));
        return rt != null && rt.getRefreshToken().equals(refreshToken);
    }

    private Map<String, Object> populateClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(getJwtProperties().getClaims().getId(), user.getUserId());
        extraClaims.put(getJwtProperties().getClaims().getRole(), user.getRole().name());
        return extraClaims;
    }


    private Key getSigningKey() {
        String key = jwtConfig.getJwtSignKey();
        try {
            PEMParser pemParser = new PEMParser(new StringReader(key));
            PemObject pemObject = pemParser.readPemObject();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
            KeyFactory keyFactory = KeyFactory.getInstance(getJwtProperties().getAlgorithm().getKeyAlgorithm());
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Unexpected error when parsing private key");
            throw new IllegalArgumentException();
        }
    }
}
