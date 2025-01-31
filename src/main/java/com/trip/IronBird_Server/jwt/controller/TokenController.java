package com.trip.IronBird_Server.jwt.controller;

import com.trip.IronBird_Server.common.exception.CustomException;
import com.trip.IronBird_Server.common.exception.ErrorCode;
import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.trip.IronBird_Server.common.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private static final ErrorCode USER_NOT_FOUND = NO_PERMISSION;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto.Reissue token) {
        // Refresh Token 검증
        if (!tokenProvider.validateToken(token.getRefreshToken())) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // Access Token에서 사용자 정보 추출
        Authentication authentication = tokenProvider.getAuthentication(token.getAccessToken());

        // Redis에서 Refresh Token 가져오기
        String key = "RefreshToken:" + authentication.getName();
        String storedRefreshToken = (String) redisTemplate.opsForValue().get(key);

        // Refresh Token 검증
        if (storedRefreshToken == null || !storedRefreshToken.equals(token.getRefreshToken())) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // 새로운 Access Token 및 Refresh Token 생성
        String email = tokenProvider.parseClaims(token.getAccessToken()).getSubject();
        User user = userRepository.findByEmail(email)
                        .orElseThrow(()-> new CustomException(USER_NOT_FOUND));
        TokenDto newToken = tokenProvider.generateTokenDto(user);

        // Redis에 새로운 Refresh Token 저장
        long ttl = (newToken.getRefreshTokenExpiresIn() - System.currentTimeMillis()) / 1000; // ms → s 변환
        redisTemplate.opsForValue().set(key, newToken.getRefreshToken(), ttl, TimeUnit.SECONDS);


        // 새로 발급된 토큰 반환
        return ResponseEntity.ok(newToken);
    }
}
