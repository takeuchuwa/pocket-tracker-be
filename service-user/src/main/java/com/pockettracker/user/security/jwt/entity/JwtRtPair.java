package com.pockettracker.user.security.jwt.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
@Getter
@Setter
@Builder
public class JwtRtPair implements Serializable {

    private String jwtId;
    private String refreshToken;

}
