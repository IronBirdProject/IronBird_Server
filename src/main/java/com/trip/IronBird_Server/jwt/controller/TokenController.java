package com.trip.IronBird_Server.jwt.controller;

import com.trip.IronBird_Server.common.exception.CustomException;
import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
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

import static com.trip.IronBird_Server.common.exception.ErrorCode.INVALID_REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenProvider tokenProvider;

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
        TokenDto newToken = tokenProvider.generateTokenDto(tokenProvider.parseClaims(token.getAccessToken()));

        // Redis에 새로운 Refresh Token 저장
        redisTemplate.opsForValue()
                .set(key, newToken.getRefreshToken(), newToken.getRefreshTokenExpiresIn() - new Date().getTime(),
                        TimeUnit.MILLISECONDS);

        // 새로 발급된 토큰 반환
        return ResponseEntity.ok(newToken);
    }
}