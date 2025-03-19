package com.trip.IronBird_Server.user.application.service;


import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.adapter.dto.KakaoUserInfoDto;

public interface KakaoService {

    public TokenDto kakaoLogin(String kakaoAccessToken);
}
