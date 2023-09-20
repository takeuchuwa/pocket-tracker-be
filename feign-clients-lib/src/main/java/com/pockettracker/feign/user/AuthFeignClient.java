package com.pockettracker.feign.user;

import com.pockettracker.feign.user.dto.JwtRefreshTokenPair;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface AuthFeignClient {

    @PostMapping("/api/v1/auth/refresh")
    JwtRefreshTokenPair refresh(@RequestBody JwtRefreshTokenPair authToken);
}
