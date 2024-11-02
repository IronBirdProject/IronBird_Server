package com.trip.IronBird_Server.kakao.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trip.IronBird_Server.kakao.dto.KakaoTokenResponseDto;
import com.trip.IronBird_Server.kakao.dto.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.security.InvalidParameterException;

@Slf4j
@Service
public class KakaoService {

    private final String clientId;
    private final WebClient webClient;

    // 생성자에서 clientId와 WebClient를 주입받습니다.
    public KakaoService(@Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                        WebClient.Builder webClientBuilder) {
        this.clientId = clientId;
        this.webClient = webClientBuilder.build();
    }

    // 카카오 API로부터 액세스 토큰을 가져오는 메서드
    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new InvalidParameterException("Invalid Parameter for Kakao API")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new InternalAuthenticationServiceException("Kakao API Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        // 액세스 토큰 및 기타 정보 로깅
        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    // 사용자 정보 조회를 위한 메서드
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        WebClient userInfoClient = webClient.mutate()
                .baseUrl("https://kapi.kakao.com") // 다른 API 엔드포인트 사용
                .build();

        KakaoUserInfoResponseDto userInfo = userInfoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/user/me")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new InvalidParameterException("Invalid Parameter for Kakao API")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new InternalAuthenticationServiceException("Kakao API Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        // 사용자 정보 로깅
        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }
}
