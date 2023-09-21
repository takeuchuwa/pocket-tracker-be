package com.pockettracker.user.auth.controller;

import com.pockettracker.user.auth.controller.dto.AuthenticationToken;
import com.pockettracker.user.auth.controller.dto.LoginRequest;
import com.pockettracker.user.auth.controller.dto.SignupRequest;
import com.pockettracker.user.auth.service.AuthenticationService;
import com.pockettracker.user.exception.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Auth controller")
public record AuthController(AuthenticationService authenticationService) {

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authenticate user with email and password", responses = {
            @ApiResponse(responseCode = "400", description = "Not valid email",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Wrong email or password", content = {@Content()})
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationToken> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest httpRequest, HttpServletResponse response) {
        AuthenticationToken auth = authenticationService.authenticate(loginRequest, httpRequest);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.ok(auth);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sign up user and authenticate", responses = {
            @ApiResponse(responseCode = "400", description = "Not valid request(some value is missing or not valid)",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "409", description = "User with this email already exists", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class))})
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthenticationToken> signup(@RequestBody @Valid SignupRequest signupRequest, HttpServletRequest httpRequest, HttpServletResponse response) throws URISyntaxException {
        AuthenticationToken auth = authenticationService.signup(signupRequest, httpRequest);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(auth);
    }

    @GetMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Refresh JWT token when", responses = {
            @ApiResponse(responseCode = "400", description = "Cookie is not present", content = {@Content()}),
            @ApiResponse(responseCode = "410", description = "Refresh token expired",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationToken> refresh(@CookieValue String authToken, @CookieValue String refreshToken, HttpServletResponse response) {
        AuthenticationToken auth = authenticationService.refresh(authToken, refreshToken);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secure() {
        return ResponseEntity.ok("ok");
    }
}
