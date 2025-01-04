package com.trip.IronBird_Server.oauth;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.domain.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String redirectUrl;

    /**
     * 인증 성공 후 필터 체인을 사용하는 메서드
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Processing authentication success with FilterChain");

        // 공통 로직 처리
        if (!processAuthenticationSuccess(request, response, authentication)) {
            return; // 오류 발생 시 필터 체인 진행 중단
        }

        // 필터 체인 진행
        chain.doFilter(request, response);
    }

    /**
     * 인증 성공 후 기본 처리 메서드
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Processing basic authentication success");

        // 공통 로직 처리
        if (!processAuthenticationSuccess(request, response, authentication)) {
            return; // 오류 발생 시 추가 작업 중단
        }

        // 리다이렉트
        response.sendRedirect(redirectUrl);
    }

    /**
     * 인증 성공 공통 로직
     */
    private boolean processAuthenticationSuccess(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 Authentication authentication) throws IOException {
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 사용자 정보 확인
            if (!(authentication.getPrincipal() instanceof CustomOAuth2User oAuth2User)) {
                log.error("Authentication principal is not an instance of CustomOAuth2User");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication principal");
                return false;
            }

            User loggedUser = oAuth2User.getUser();
            log.info("Successfully authenticated user: {}", loggedUser.getEmail());

            // JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(createUserPayload(loggedUser));
            if (tokenDto == null || tokenDto.getAccessToken() == null) {
                log.error("Failed to generate JWT token");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate token");
                return false;
            }

            log.info("JWT Token generated successfully: {}", tokenDto);

            // 리다이렉트 URL 생성
            String redirectUri = buildRedirectUri(tokenDto);
            log.info("Redirecting to: {}", redirectUri);

            // 리다이렉트 URL 설정
            response.setHeader("Location", redirectUri);
            return true;

        } catch (Exception e) {
            log.error("Error during authentication success processing", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during authentication");
            return false;
        }
    }

    /**
     * 사용자 정보를 기반으로 JWT 페이로드 생성
     */
    private Map<String, Object> createUserPayload(User user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", user.getId());
        payload.put("email", user.getEmail());
        payload.put("name", user.getName());
        payload.put("picture", user.getProfilePic());
        payload.put("role", "ROLE_USER");

        return payload;
    }

    /**
     * 리다이렉트 URI 생성 메서드
     */
    private String buildRedirectUri(TokenDto tokenDto) {
        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("grantType", tokenDto.getGrantType())
                .queryParam("accessToken", tokenDto.getAccessToken())
                .queryParam("accessTokenExpiresIn", tokenDto.getAccessTokenExpiresIn())
                .queryParam("refreshToken", tokenDto.getRefreshToken())
                .queryParam("refreshTokenExpiresIn", tokenDto.getRefreshTokenExpiresIn())
                .build()
                .toUriString();
    }
}
