package com.trip.IronBird_Server.oauth;

import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import com.trip.IronBird_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //기본 OAuth2UserService를 통해 사용자 정보를 가져온다.
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 어떤 OAuth2 플랫폼(Google, Kakao, Naver)에서 요청이 들어왔는지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();    //Google, Naver, Kakao 구분
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes;

        if ("google".equals(registrationId)) {
            oAuth2Attributes = extractGoogleAttributes(attributes);
        } else if ("kakao".equals(registrationId)) {
            oAuth2Attributes = extractKakaoAttributes(attributes);
        } else if ("naver".equals(registrationId)) {
            oAuth2Attributes = extractNaverAttributes(attributes);
        } else {
            throw new OAuth2AuthenticationException("Unsupported registrationId: " + registrationId);
        }

        String email = oAuth2Attributes.getEmail();
        String name = oAuth2Attributes.getName();
        String picture = oAuth2Attributes.getPicture();

        //DB에 회원 정보를 저장하거나 기존 회원 정보를 업데이트
        User loggedUser = userRepository.findByEmail(email).orElseGet(() -> {
            log.info("Creating new user with picture: {}", picture);
            return userRepository.save(
                    User.createSocialUser(
                            email,
                            oAuth2Attributes.getOauth2Id(),
                            OauthType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()),
                            picture
                    )
            );
        });

        return new CustomOAuth2User(oAuth2Attributes.getAttributes(),loggedUser);
    }

    // Google 사용자 정보 처리
    private OAuth2Attributes extractGoogleAttributes(Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .oauth2Id((String) attributes.get("sub"))  // Google의 고유 ID
                .email((String) attributes.get("email"))  // 이메일
                .name((String) attributes.get("name"))    // 이름
                .picture((String) attributes.get("picture")) // 프로필 사진
                .attributes(attributes)  // 전체 속성
                .build();
    }

    // Kakao 사용자 정보 처리
    private OAuth2Attributes extractKakaoAttributes(Map<String, Object> attributes) {
        // Kakao는 kakao_account라는 하위 객체 안에 사용자 정보가 있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .oauth2Id(String.valueOf(attributes.get("id")))  // Kakao의 고유 ID
                .email((String) kakaoAccount.get("email"))       // 이메일
                .name((String) profile.get("nickname"))          // 닉네임
                .picture((String) profile.get("profile_image_url")) // 프로필 사진
                .attributes(attributes)  // 전체 속성
                .build();
    }

    // Naver 사용자 정보 처리
    private OAuth2Attributes extractNaverAttributes(Map<String, Object> attributes) {
        // Naver는 response라는 하위 객체 안에 사용자 정보가 있음
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .oauth2Id((String) response.get("id"))         // Naver의 고유 ID
                .email((String) response.get("email"))         // 이메일
                .name((String) response.get("name"))           // 이름
                .picture((String) response.get("profile_image")) // 프로필 사진
                .attributes(attributes)  // 전체 속성
                .build();
    }
}
