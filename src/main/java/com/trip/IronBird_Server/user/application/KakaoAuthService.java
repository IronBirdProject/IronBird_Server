package com.trip.IronBird_Server.user.application;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.adapter.dto.KakaoUserInfoDto;
import com.trip.IronBird_Server.user.application.service.KakaoService;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService implements KakaoService {

    private final KakaoApiClientImp kakaoApiClientImp;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;


    @Override
    public TokenDto kakaoLogin(String kakaoAccessToken){
        // kakao 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfoDto = kakaoApiClientImp.getKakaoUserInfo(kakaoAccessToken);

        // 기존 회원 확인
        Optional<User> userOptional = userRepository.findByOauthIdAndOauthType(
                String.valueOf(kakaoUserInfoDto.getId()), OauthType.KAKAO
        );

        User user;
        if (userOptional.isPresent()){
            user = userOptional.get();
        }else {
            user = User.builder()
                    .email(kakaoUserInfoDto.getEmail())
                    .name(kakaoUserInfoDto.getNickname())
                    .profilePic(kakaoUserInfoDto.getProfileImageUrl())
                    .oauthId(String.valueOf(kakaoUserInfoDto.getId()))
                    .oauthType(OauthType.KAKAO)
                    .build();

            userRepository.save(user);
        }

        return tokenProvider.generateTokenDto(user);
    }



}
