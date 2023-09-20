package com.pockettracker.feign;

import lombok.Getter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@Getter
@AutoConfiguration
@ComponentScan(basePackages = "com.pockettracker.feign")
@EnableFeignClients(basePackages = "com.pockettracker.feign")
public class FeignConfig {
}
