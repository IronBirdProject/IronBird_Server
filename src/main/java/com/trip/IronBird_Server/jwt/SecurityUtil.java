package com.trip.IronBird_Server.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil(){

    }

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    //Guest 일 경우 예외 대신 게스트 Id를 반환
    public static Long getCurrentMemberId(){
        if(isGuest()){
            log.info("게스트 사용자입니다.");
            return 0L;
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return Long.parseLong(authentication.getName());
    }

    //현재 사용자가 게스트인지 확인
    public static boolean isGuest() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser"));
    }

}
