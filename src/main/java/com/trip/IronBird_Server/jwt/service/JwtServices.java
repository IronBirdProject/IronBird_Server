package com.trip.IronBird_Server.jwt.service;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.trip.IronBird_Server.jwt.TokenProvider.AUTHORITIES_KEY;

@Service
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

        // 사용자 권한 설정
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail());  // subject
        claims.put("role", "ROLE_USER");    // 권한 정보

        // JWT 토큰 생성
        return tokenProvider.generateTokenDto(claims);
    }

    // Access Token 재발급
    public TokenDto refreshToken(String refreshToken) {
        // 토큰 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        // Redis에서 사용자 Refresh Token 확인
        Claims claims = tokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();

        String storedToken = (String) redisTemplate.opsForValue().get("RefreshToeken: " + email);
        if (!refreshToken.equals(storedToken)) {
            throw new RuntimeException("Refresh Token이 일치하지 않습니다.");
        }

        // 새로운 Access Token 생성
        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("sub", email);
        newClaims.put("role", claims.get(AUTHORITIES_KEY));

        return tokenProvider.generateTokenDto(newClaims);
    }
}