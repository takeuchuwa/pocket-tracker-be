package com.pockettracker.user.auth.service;

import com.pockettracker.user.auth.controller.dto.AuthenticationToken;
import com.pockettracker.user.auth.controller.dto.LoginRequest;
import com.pockettracker.user.auth.controller.dto.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URISyntaxException;

public interface AuthenticationService {

    AuthenticationToken authenticate(LoginRequest loginRequest, HttpServletRequest httpRequest);

    AuthenticationToken signup(SignupRequest signupRequest, HttpServletRequest httpRequest) throws URISyntaxException;

    AuthenticationToken refresh(String jwtToken, String refreshToken);

    void addAuthCookies(HttpServletResponse response, AuthenticationToken auth);
}
