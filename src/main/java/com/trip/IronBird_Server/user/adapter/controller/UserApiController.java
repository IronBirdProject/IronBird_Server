package com.trip.IronBird_Server.user.adapter.controller;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.user.adapter.dto.KakaoUserInfoDto;
import com.trip.IronBird_Server.user.application.KakaoAuthService;
import com.trip.IronBird_Server.user.infrastructure.KakaoApiClientImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserApiController {

    private final KakaoAuthService kakaoAuthService;
    private final TokenProvider tokenProvider;

    /**
     * KakaoLogin
     * @param requestBody
     * @return
     */
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> requestBody){
        String kakaoAccessToken = requestBody.get("access_token");

        //kakao 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfoDto = kakaoAuthService.authenticate(kakaoAccessToken);

        //JWT 발급

        return null;
    }

}
