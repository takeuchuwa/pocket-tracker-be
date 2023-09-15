package com.pockettracker.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends UserServiceException {
    public ConflictException(String message) {
        super(message);
    }
}
