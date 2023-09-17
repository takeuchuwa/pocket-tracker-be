package com.pockettracker.jwt.util;

public class JwtConstants {
    public static final String BEARER = "Bearer ";
    public static final int BEARER_TOKEN_START = 7;

    private JwtConstants() {
        throw new UnsupportedOperationException("Constants class");
    }
}
