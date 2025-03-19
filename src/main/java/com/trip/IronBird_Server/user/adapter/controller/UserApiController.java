package com.trip.IronBird_Server.user.adapter.controller;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.adapter.dto.KakaoUserInfoDto;
import com.trip.IronBird_Server.user.application.KakaoAuthService;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import com.trip.IronBird_Server.user.infrastructure.KakaoApiClientImp;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserApiController {

    private final KakaoAuthService kakaoAuthService;

    /**
     * KakaoLogin
     * @param requestBody
     * @return
     */
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> requestBody){
        String kakaoAccessToken = requestBody.get("access_token");

        TokenDto tokenDto =kakaoAuthService.kakaoLogin(kakaoAccessToken);
        return ResponseEntity.ok(tokenDto);
    }

}
