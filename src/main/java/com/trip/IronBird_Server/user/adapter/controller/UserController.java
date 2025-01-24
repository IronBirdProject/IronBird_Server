package com.trip.IronBird_Server.user.adapter.controller;

import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.jwt.service.JwtServices;
import com.trip.IronBird_Server.user.adapter.dto.RegisterDto;
import com.trip.IronBird_Server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtServices jwtService;



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        //회원가입 처리
        try {
            userService.registerUser(
                    registerDto.getEmail(),
                    registerDto.getPassword(),
                    registerDto.getName(),
                    registerDto.getDefaultProfilePic()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody RegisterDto loginDto){
        TokenDto tokenDto = jwtService.login(loginDto.getEmail(), loginDto.getPassword());

        return ResponseEntity.ok(tokenDto);
    }
}
