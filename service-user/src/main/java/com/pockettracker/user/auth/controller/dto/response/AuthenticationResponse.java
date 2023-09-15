package com.pockettracker.user.auth.controller.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String jwtToken) {
}
