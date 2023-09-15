package com.pockettracker.user.exception.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record ErrorResponse(String message, Map<String, String> errorMessages) {
}
