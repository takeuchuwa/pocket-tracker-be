package com.pockettracker.user.auth.service;

import com.pockettracker.user.auth.controller.dto.request.LoginRequest;
import com.pockettracker.user.auth.controller.dto.request.SignupRequest;
import com.pockettracker.user.auth.controller.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URISyntaxException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(LoginRequest loginRequest, HttpServletRequest httpRequest);

    AuthenticationResponse signup(SignupRequest signupRequest, HttpServletRequest httpRequest) throws URISyntaxException;

    AuthenticationResponse refresh(String jwtToken, String refreshToken);

    void addAuthCookies(HttpServletResponse response, AuthenticationResponse auth);
}
