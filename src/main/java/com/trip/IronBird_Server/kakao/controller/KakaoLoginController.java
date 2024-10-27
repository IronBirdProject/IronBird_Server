package com.trip.IronBird_Server.kakao.controller;

import com.trip.IronBird_Server.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class KakaoLoginController {

    private final KakaoService kakaoService;


//    @GetMapping("/callback")
//    public ResponseEntity<?> getKakaoAuthorizeCode(@RequestParam("code") String authorizeCode, @RequestParam(value = "type", defaultValue = "kakao") String type) {
//        log.info("[{} login] authorizeCode : {}", type, authorizeCode);
//        return kakaoService.signIn(authorizeCode, type);
//    }


}
