package com.trip.IronBird_Server.oauth.service.strategy;

import com.trip.IronBird_Server.oauth.info.OAuth2UserInfo;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoOAuth2Strategy implements OAuth2Strategy {
    @Override
    public OauthType getOAuth2ProviderType() {
        return OauthType.Kakao;
    }

    @Override
    public OAuth2UserInfo getUserInfo(OAuth2User user) {
        final Map<String, Object> attributes = user.getAttributes();
        final String oauthId = String.valueOf(attributes.get("id"));
        final String oauthName = String.valueOf(((Map<String, Object>) attributes.get("properties")).get("nickname"));

        return new OAuth2UserInfo(OauthType.Kakao, oauthId, oauthName);
    }
}
