package com.trip.IronBird_Server.oauth.service.strategy;


import com.trip.IronBird_Server.oauth.info.OAuth2UserInfo;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Strategy {
    OauthType getOAuth2ProviderType();

    default void isOauthIdExist(String oauthId) {
        if (null == oauthId) {
            throw new OAuth2AuthenticationException("oauthId does not exist");
        }
    }

    OAuth2UserInfo getUserInfo(OAuth2User oauth2User);
}
