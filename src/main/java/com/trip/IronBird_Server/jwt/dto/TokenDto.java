package com.trip.IronBird_Server.jwt.dto;

import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
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

    private final UserInfo user;    // 사용자 정보 포함

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo{
        private String email;
        private String name;
        private String oauthId;
        private String profilePic;

        public UserInfo(Long id, String email, String name, OauthType oauthType, String profilePic) {
            this.email = email;
            this.name = name;
            this.oauthId = oauthType != null ? oauthType.name() : null;
            this.profilePic = profilePic;
        }

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reissue{
        private String accessToken;
        private String refreshToken;
    }

}
