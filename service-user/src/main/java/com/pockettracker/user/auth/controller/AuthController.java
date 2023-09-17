package com.pockettracker.user.auth.controller;

import com.pockettracker.user.auth.controller.dto.request.LoginRequest;
import com.pockettracker.user.auth.controller.dto.request.SignupRequest;
import com.pockettracker.user.auth.controller.dto.response.AuthenticationResponse;
import com.pockettracker.user.auth.service.AuthenticationService;
import com.pockettracker.user.auth.util.AuthConstants;
import com.pockettracker.user.exception.ConflictException;
import com.pockettracker.user.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest httpRequest, HttpServletResponse response) {
        AuthenticationResponse auth = authenticationService.authenticate(loginRequest, httpRequest);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody @Valid SignupRequest signupRequest, HttpServletRequest httpRequest, HttpServletResponse response) throws URISyntaxException {
        AuthenticationResponse auth = authenticationService.signup(signupRequest, httpRequest);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@CookieValue(AuthConstants.AUTH_TOKEN) String jwtToken, @CookieValue(AuthConstants.REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        AuthenticationResponse auth = authenticationService.refresh(jwtToken, refreshToken);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.ok(auth);
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
