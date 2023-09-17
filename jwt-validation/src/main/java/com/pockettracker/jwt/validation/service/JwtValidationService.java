package com.pockettracker.jwt.validation.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtValidationService {

    boolean isTokenValid(String token);

    Claims extractAllClaims(String token);

    String extractUsername(String token);

    String extractId(String token);

    Collection<GrantedAuthority> extractAuthorities(String token);
}
