package com.trip.IronBird_Server.user.application.service;

import com.trip.IronBird_Server.user.adapter.dto.KakaoUserInfoDto;

public interface KakaoApiClient {

    KakaoUserInfoDto getKakaoUserInfo(String accessToken);
}
