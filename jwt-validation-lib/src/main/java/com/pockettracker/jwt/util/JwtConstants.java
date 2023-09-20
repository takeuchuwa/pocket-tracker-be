package com.pockettracker.jwt.util;

public class JwtConstants {
    public static final String BEARER = "Bearer ";
    public static final int BEARER_TOKEN_START = 7;

    public static final String AUTH_TOKEN = "authToken";
    public static final String REFRESH_TOKEN = "refreshToken";

    private JwtConstants() {
        throw new UnsupportedOperationException("Constants class");
    }
}
