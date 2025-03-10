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
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

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

        // 이메일로 사용자 검색
        Optional<User> userOptional = userRepository.findByEmail(kakaoUserInfoDto.getEmail());

        User user;
        if(userOptional.isPresent()){
            user = userOptional.get(); // 기존 사용자
        }else {
            // 신규 사용자 등록
            user = User.builder()
                    .id((kakaoUserInfoDto.getId()))
                    .email(kakaoUserInfoDto.getEmail())
                    .name(kakaoUserInfoDto.getNickname())
                    .profilePic(kakaoUserInfoDto.getProfileImageUrl())
                    .oauthType(OauthType.KAKAO)
                    .build();
        }

        //Generate JWT
        TokenDto tokenDto = tokenProvider.generateTokenDto(user);


        // return JWT
        return ResponseEntity.ok(tokenDto);
    }

}
