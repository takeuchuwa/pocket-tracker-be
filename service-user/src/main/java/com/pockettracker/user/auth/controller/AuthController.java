package com.pockettracker.user.auth.controller;

import com.pockettracker.user.auth.controller.dto.request.LoginRequest;
import com.pockettracker.user.auth.controller.dto.request.SignupRequest;
import com.pockettracker.user.auth.controller.dto.response.AuthenticationResponse;
import com.pockettracker.user.auth.service.AuthenticationService;
import com.pockettracker.user.auth.util.AuthConstants;
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
            @ApiResponse(responseCode = "403", description = "Wrong email or password")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest httpRequest, HttpServletResponse response) {
        AuthenticationResponse auth = authenticationService.authenticate(loginRequest, httpRequest);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.ok(auth);
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sign up user and authenticate", responses = {
            @ApiResponse(responseCode = "400", description = "Not valid request(some value is missing or not valid)",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "409", description = "User with this email already exists")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody @Valid SignupRequest signupRequest, HttpServletRequest httpRequest, HttpServletResponse response) throws URISyntaxException {
        AuthenticationResponse auth = authenticationService.signup(signupRequest, httpRequest);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(auth);
    }

    @GetMapping("/refresh")
    @Operation(summary = "Refresh JWT token when", responses = {
            @ApiResponse(responseCode = "400", description = "Cookies is not present")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationResponse> refresh(@CookieValue(AuthConstants.AUTH_TOKEN) String jwtToken, @CookieValue(AuthConstants.REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        AuthenticationResponse auth = authenticationService.refresh(jwtToken, refreshToken);
        authenticationService.addAuthCookies(response, auth);
        return ResponseEntity.ok(auth);
    }

}
