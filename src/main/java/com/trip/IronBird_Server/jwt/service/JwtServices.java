package com.trip.IronBird_Server.jwt.service;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.trip.IronBird_Server.jwt.TokenProvider.AUTHORITIES_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServices {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;


    // 로그인 처리 및 토큰 발급
    public TokenDto login(String email, String password) {
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        TokenDto tokenDto = tokenProvider.generateTokenDto(user);

        // 현재 시간을 밀리초 단위로 가져옴
        long currentTimeMillis = System.currentTimeMillis();

        // RT (Refresh Token) TTL 계산
        long ttlSeconds = (tokenDto.getRefreshTokenExpiresIn() - currentTimeMillis) / 1000;

        if(ttlSeconds <= 0){
            ttlSeconds = 7*24*60*60;
        }


        // Redis에 Refresh 저장
        String redisKey = "RefreshToken: " + String.valueOf(user.getId()).trim();
        redisTemplate.opsForValue().set(redisKey, tokenDto.getRefreshToken(), ttlSeconds, TimeUnit.SECONDS);
        log.info("Saved RefreshToken in Redis : key={}, value={}, ttl={} seconds", redisKey,tokenDto.getRefreshToken(),ttlSeconds);
        return tokenDto;
    }


    public Long extractUserId(String token) {

        try {// "Bearer " 제거
            String cleanedToken = token.replace("Bearer ", "");
            log.info("Cleaned token: {}", cleanedToken);

            // Claims 추출
            Claims claims = tokenProvider.parseClaims(cleanedToken);
            log.info("Extracted claims: {}", claims);

            // 사용자 ID 추출
            Long userId = Long.parseLong(claims.get("userId").toString());
            log.info("Extracted userId from claims: {}", userId);


            return userId;
        }catch (Exception e){
            log.error("Failed to extract userId from token: {}", e.getMessage(), e);

            throw e;
        }
    }

}