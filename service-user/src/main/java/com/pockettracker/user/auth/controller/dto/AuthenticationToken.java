package com.pockettracker.user.auth.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthenticationToken(@NotNull String authToken, @NotNull String refreshToken) {
}
