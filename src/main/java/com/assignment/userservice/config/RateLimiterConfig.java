package com.assignment.userservice.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Bean
    public RateLimiter rate100PerOneMinuteLimiter() {
        return RateLimiter.create(100.0 / 60.0); // 1분당 100회
    }

    @Bean
    public RateLimiter rate500PerOneMinuteLimiter() {
        return RateLimiter.create(500.0 / 60.0); // 1분당 100회
    }

}
