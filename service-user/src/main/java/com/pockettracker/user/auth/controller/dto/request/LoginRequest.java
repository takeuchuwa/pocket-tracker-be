package com.pockettracker.user.auth.controller.dto.request;

import com.pockettracker.user.auth.controller.dto.request.validation.ExtendedEmailValidator;
import lombok.Builder;

@Builder
public record LoginRequest(@ExtendedEmailValidator String email,
                           String password) {
}
