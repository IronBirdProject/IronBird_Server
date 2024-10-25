package com.trip.IronBird_Server.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLoginDto {
    private String accessToken;
    private String refreshToken;
}
