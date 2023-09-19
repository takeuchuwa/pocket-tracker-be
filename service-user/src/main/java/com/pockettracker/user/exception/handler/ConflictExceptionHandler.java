package com.pockettracker.user.exception.handler;

import com.pockettracker.user.exception.ConflictException;
import com.pockettracker.user.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ConflictExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {ConflictException.class})
    protected ResponseEntity<Object> handleConflict(
            ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .errorMessages(ex.getErrorMessages())
                        .build());
    }
}
