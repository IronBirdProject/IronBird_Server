package com.trip.IronBird_Server.kakao.service;

import com.trip.IronBird_Server.kakao.dto.KakaoTokenResponseDto;
import com.trip.IronBird_Server.kakao.dto.KakaoUserInfoResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.InvalidParameterException;

@Slf4j
@Service
public class KakaoService {

    private final String clientId;
    private final String redirectUri;
    private final WebClient kakaoAuthClient;
    private final WebClient kakaoApiClient;

    // 생성자에서 clientId와 redirectUri 및 WebClient 인스턴스를 주입받습니다.
    public KakaoService(
            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri,
            WebClient.Builder webClientBuilder) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.kakaoAuthClient = webClientBuilder
                .baseUrl("https://kauth.kakao.com") // 인증 URL 설정
                .build();
        this.kakaoApiClient = webClientBuilder
                .baseUrl("https://kapi.kakao.com") // API URL 설정
                .build();
    }

    // 카카오 API로부터 액세스 토큰을 가져오는 메서드
    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoAuthClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
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
        KakaoUserInfoResponseDto userInfo = kakaoApiClient.get()
                .uri("/v2/user/me")
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

    //Kakao Logout 메소드
    public void KakaoLogout(String accesstoken){
        KakaoUserInfoResponseDto kakaoUserInfoResponseDto = kakaoApiClient.post()
                .uri("/v1/user/logout")
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + accesstoken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new InvalidParameterException("Invalid Parameter for Kakao API")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new InternalAuthenticationServiceException("Kakao API Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();
        log.info("[Kakao Service] Logout Successful for Access Token: {}", accesstoken);

    }
}

