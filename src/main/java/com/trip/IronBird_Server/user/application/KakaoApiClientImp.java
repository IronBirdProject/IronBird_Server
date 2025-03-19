package com.trip.IronBird_Server.user.application;

import com.trip.IronBird_Server.user.adapter.dto.KakaoUserInfoDto;
import com.trip.IronBird_Server.user.application.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoApiClientImp implements KakaoService {

    private final RestTemplate restTemplate;


    @Override
    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        if(response.getStatusCode() == HttpStatus.OK){
            return parseKakaoUser(response.getBody());
        }else {
            throw new RuntimeException("카카오 사용자 정보 요청 실패");
        }
    }

    private KakaoUserInfoDto parseKakaoUser(String responseBody){
        JSONObject json = new JSONObject(responseBody);
        JSONObject kakaoAccount = json.getJSONObject("kakao_account");
        JSONObject profile = json.getJSONObject("profile");

        return new KakaoUserInfoDto(
                json.getLong("id"),
                kakaoAccount.getString("email"),
                profile.getString("nickname"),
                profile.getString("profileImageUrl")
        );
    }
}
