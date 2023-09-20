package com.pockettracker.jwt.validation.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

public interface JwtValidationService {

    boolean isTokenValid(String token);

    boolean isTokenValid(String token, String cookies);

    Claims extractAllClaims(String token);

    String extractUsername(String token);

    String extractId(String token);

    Long extractUserId(String token);

    Collection<GrantedAuthority> extractAuthorities(String token);

    Date extractExpiration(String token);
}
