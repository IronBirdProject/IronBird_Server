package com.trip.IronBird_Server.user.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;

}
