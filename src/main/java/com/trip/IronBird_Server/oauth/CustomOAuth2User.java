package com.trip.IronBird_Server.oauth;

import com.trip.IronBird_Server.user.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private User user;
    private boolean first;

    public CustomOAuth2User( Map<String, Object> attributes, User user) {
        super(List.of(new SimpleGrantedAuthority("ROLE_USER")), attributes,"sub");
        this.user = user;
        this.first = first;
    }

    //시큐리티 컨텍스트 내의 인증 정보를 가져와 하는 작업을 수행할 경우
    //계정 식별자가 사용되도록 조치
    @Override
    public String getName(){
        return String.valueOf(user.getId());
    }
}
