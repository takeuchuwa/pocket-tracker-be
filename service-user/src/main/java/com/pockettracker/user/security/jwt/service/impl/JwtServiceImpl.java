package com.pockettracker.user.security.jwt.service.impl;


import com.pockettracker.jwt.properties.JwtProperties;
import com.pockettracker.jwt.validation.service.impl.JwtValidationServiceImpl;
import com.pockettracker.user.entity.User;
import com.pockettracker.user.security.jwt.config.JwtConfig;
import com.pockettracker.user.security.jwt.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
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
public class JwtServiceImpl extends JwtValidationServiceImpl implements JwtService {

    private final JwtConfig jwtConfig;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtServiceImpl(JwtConfig jwtConfig, RedisTemplate<String, String> redisTemplate, JwtProperties jwtProperties) {
        super(jwtConfig.getJwtPublicKey(), jwtProperties);
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
        String refreshToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(extractId(jwtToken), refreshToken);
        return refreshToken;
    }

    @Override
    public boolean isJwtRtPairValid(String jwtToken, String refreshToken) {
        String rt = redisTemplate.opsForValue().getAndDelete(extractId(jwtToken));
        return rt != null && rt.equals(refreshToken);
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
