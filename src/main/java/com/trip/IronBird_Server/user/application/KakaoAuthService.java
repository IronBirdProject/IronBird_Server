package com.trip.IronBird_Server.user.application;

import com.trip.IronBird_Server.user.adapter.dto.KakaoUserInfoDto;
import com.trip.IronBird_Server.user.infrastructure.KakaoApiClientImp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoApiClientImp kakaoApiClientImp;

    public KakaoUserInfoDto authenticate(String accessToken){
        return kakaoApiClientImp.getKakaoUserInfo(accessToken);
    }


}
