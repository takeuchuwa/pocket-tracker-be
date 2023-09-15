package com.pockettracker.user.jwt.service;

import com.pockettracker.user.entity.User;

public interface JwtService {

    String generateToken(User user);

}
