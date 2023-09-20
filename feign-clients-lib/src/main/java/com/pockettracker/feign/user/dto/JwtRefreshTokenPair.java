package com.pockettracker.feign.user.dto;

import lombok.Builder;

@Builder
public record JwtRefreshTokenPair(String authToken, String refreshToken) {
}
