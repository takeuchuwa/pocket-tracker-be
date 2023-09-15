package com.pockettracker.user.auth.service;

import com.pockettracker.user.auth.controller.dto.request.LoginRequest;
import com.pockettracker.user.auth.controller.dto.request.SignupRequest;
import com.pockettracker.user.auth.controller.dto.response.AuthenticationResponse;

import java.net.URISyntaxException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(LoginRequest request);

    AuthenticationResponse signup(SignupRequest request) throws URISyntaxException;
}
