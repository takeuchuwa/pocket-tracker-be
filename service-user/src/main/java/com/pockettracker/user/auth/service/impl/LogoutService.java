package com.pockettracker.user.auth.service.impl;

import com.pockettracker.jwt.util.JwtConstants;
import com.pockettracker.jwt.validation.service.JwtValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public record LogoutService(JwtValidationService jwtValidationService, RedisTemplate<String, String> redisTemplate) implements LogoutHandler {

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(JwtConstants.BEARER)) {
            return;
        }
        String jwt = authHeader.substring(JwtConstants.BEARER_TOKEN_START);
        redisTemplate.opsForValue().getAndDelete(String.valueOf(jwtValidationService.extractUserId(jwt)));
    }
}
