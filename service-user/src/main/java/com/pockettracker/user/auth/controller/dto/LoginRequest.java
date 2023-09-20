package com.pockettracker.user.auth.controller.dto;

import com.pockettracker.user.auth.controller.dto.validation.ExtendedEmailValidator;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginRequest(@NotNull @ExtendedEmailValidator String email,
                           @NotNull String password) {
}
