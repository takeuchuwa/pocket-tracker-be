package com.pockettracker.auth.jwt.service;

import com.pockettracker.auth.user.entity.User;

public interface JwtService {

    String generateToken(User user);

}
