package com.trip.IronBird_Server.oauth.info;

import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UserInfo {

    private final OauthType oauthType;
    private final String oauthId;
    private final String name;

}
