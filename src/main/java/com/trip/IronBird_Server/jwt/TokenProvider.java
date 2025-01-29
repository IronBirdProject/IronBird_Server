package com.trip.IronBird_Server.jwt;

import com.trip.IronBird_Server.jwt.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {
    public static final String AUTHORITIES_KEY = "role";
    private static final String BEARER_TYPE = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 30 * 24 * 7;
    private final Key key;
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, RedisTemplate<String, Object> redisTemplate) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    public TokenDto generateTokenDto(Map<String, Object> claims){
        long now = (new Date()).getTime();

        //AccessToken 생성
        Date expiresIn = new Date(now  + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .addClaims(claims)
                .setExpiration(expiresIn)
                .signWith(key)
                .compact();

        log.info("Generated Access Token Claims: {} ", claims);

        //RefreshToken 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setClaims(Map.of("sub", claims.get("sub")))
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key)
                .compact();

        //Redis에 Refresh Token 저장
        redisTemplate.opsForValue()
                .set("RefreshToeken: " + claims.get("sub"), refreshToken,
                        refreshTokenExpiresIn.getTime() - now,
                        TimeUnit.MILLISECONDS);

        //TokenDto 객체 반환
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(expiresIn.getTime())
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 권한 정보가 없을 경우 기본 권한을 설정하거나 예외 처리
        Collection<? extends GrantedAuthority> authorities;
        if (claims.get(AUTHORITIES_KEY) == null || claims.get(AUTHORITIES_KEY).toString().isEmpty()) {
            log.warn("경고: 권한 정보가 없는 토큰입니다. 기본 USER 권한을 설정합니다.");
            authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));  // 기본 권한 설정
        } else {
            // 클레임에서 권한 정보 가져오기
            authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        // UserDetails 객체를 만들어서 Authentication 반환
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명 또는 형식입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 비어 있거나 잘못되었습니다.");
        }
        return false;
    }
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
