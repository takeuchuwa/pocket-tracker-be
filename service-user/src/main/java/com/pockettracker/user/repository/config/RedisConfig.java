package com.pockettracker.user.repository.config;

import com.pockettracker.user.security.jwt.entity.JwtRtPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${app.redis.host:localhost}")
    private String redisHost;

    @Value("${app.redis.port:6379}")
    private int redisPort;

    @Value("${app.redis.password}")
    private String redisPassword;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, JwtRtPair> redisJwtRtPairTemplate() {
        RedisTemplate<String, JwtRtPair> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
