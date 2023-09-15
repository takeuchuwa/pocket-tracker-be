package com.pockettracker.user.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UserServiceException extends RuntimeException {
    private final String message;
    private final Map<String, String> errorMessages;

    public UserServiceException() {
        this("An unexpected error occurred, please try again later");
    }
    public UserServiceException(String message) {
        this.message = message;
        this.errorMessages = new HashMap<>();
    }

    public UserServiceException(String message, Map<String, String> errorMessages) {
        this.message = message;
        this.errorMessages = errorMessages;
    }
}
