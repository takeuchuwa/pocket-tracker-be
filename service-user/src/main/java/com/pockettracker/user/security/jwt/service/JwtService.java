package com.pockettracker.user.security.jwt.service;

import com.pockettracker.jwt.validation.service.JwtValidationService;
import com.pockettracker.user.entity.User;


public interface JwtService extends JwtValidationService {

    String generateToken(User user);

    String generateJwtRtPair(String jwtToken);

    boolean isJwtRtPairValid(String jwtToken, String refreshToken);
}
