package com.trip.IronBird_Server.config;

public class AllowedUrls {

    public static final String[] allowUrls = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v1/posts/**",
            "/api/v1/replies/**",
            "/login",
            "/login/**",
            "/auth/login/kakao",
            "/auth/**",
            "/api/auth/**", // Use 'requestMatchers' instead of 'antMatchers'
            "/api/test/all"
    };
}
