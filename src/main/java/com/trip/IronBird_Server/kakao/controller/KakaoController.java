package com.trip.IronBird_Server.kakao.controller;

import com.trip.IronBird_Server.kakao.dto.KakaoUserInfoResponseDto;
import com.trip.IronBird_Server.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    //카카오 로그인 메소드
    @GetMapping("/auth/login/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        // User 로그인, 또는 회원가입 로직 추가
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //카카오 로그아웃 메소드
    @GetMapping("/auth/logout/kakao")
    public ResponseEntity<?> logout(@RequestParam("accesstoken") String accesstoken){
        kakaoService.KakaoLogout(accesstoken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}