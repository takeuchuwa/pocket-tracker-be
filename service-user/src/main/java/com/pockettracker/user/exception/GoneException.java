package com.pockettracker.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.CONFLICT)
public class GoneException extends UserServiceException {
    public GoneException(String message) {
        super(message);
    }
}
