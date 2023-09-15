package com.pockettracker.user.auth.controller;

import com.pockettracker.user.auth.controller.dto.request.LoginRequest;
import com.pockettracker.user.auth.controller.dto.request.SignupRequest;
import com.pockettracker.user.auth.controller.dto.response.AuthenticationResponse;
import com.pockettracker.user.auth.service.AuthenticationService;
import com.pockettracker.user.exception.ConflictException;
import com.pockettracker.user.exception.response.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/auth")
public record AuthController(AuthenticationService authenticationService) {

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody @Valid SignupRequest request) throws URISyntaxException {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictException(
            ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .errorMessages(ex.getErrorMessages())
                        .build());
    }
}
