package com.trip.IronBird_Server.auth.controller;

import com.trip.IronBird_Server.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @GetMapping("/auth/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse){
        //User user = authService.oAuthLogin(accessCode, httpServletResponse);

        return null;
    }
}
