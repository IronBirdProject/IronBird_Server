package com.trip.IronBird_Server.oauth.info;

import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Member;
import java.util.Map;

@Getter
@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile
) {
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes){
        return switch (registrationId){
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);

            default -> throw new IllegalStateException();
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickcname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .build();
    }
}
