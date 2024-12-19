package com.trip.IronBird_Server.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class TokenDto {

    private final String grantType;
    private final String accessToken;
    private final Long accessTokenExpiresIn;
    private final Long refreshTokenExpiresIn;
    private final String refreshToken;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reissue{
        private String accessToken;
        private String refreshToken;
    }

}
