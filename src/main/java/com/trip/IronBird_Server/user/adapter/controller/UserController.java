package com.trip.IronBird_Server.user.adapter.controller;

import com.trip.IronBird_Server.common.custom.CustomUserDetails;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.jwt.service.JwtServices;
import com.trip.IronBird_Server.user.adapter.dto.LoginDto;
import com.trip.IronBird_Server.user.adapter.dto.RegisterDto;
import com.trip.IronBird_Server.user.adapter.dto.UserDto;
import com.trip.IronBird_Server.user.application.service.UserService;
import com.trip.IronBird_Server.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtServices jwtService;


    /**
     * 회원가입 컨트롤러
     * @param registerDto
     * @return
     */

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

    /**
     * 로그인 컨트롤러
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @RequestBody LoginDto loginDto){
        TokenDto tokenDto = jwtService.login(loginDto.getUserName(), loginDto.getPassword());

        return ResponseEntity.ok(tokenDto);
    }


    /**
     * 회원정보 수정
     * @param id
     * @return
     */
    @PutMapping("/user/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,
                                        @RequestBody UserDto userDto,
                                        @RequestHeader("Authorization") String Bearer) {
        try {
            // 토큰 값 로그
            log.info("Received token: {}", Bearer);

            // JWT에서 사용자 ID 추출
            Long userId = jwtService.extractUserId(Bearer);
            log.info("Extracted userId from token: {}", userId);

            // URL의 id와 JWT userId 비교 로그
            if (!id.equals(userId)) {
                log.warn("Mismatch between URL id ({}) and token userId ({})", id, userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            // 서비스 호출
            User updatedUser = userService.updateUser(userId, userDto);
            log.info("User successfully updated: {}", updatedUser);

            return ResponseEntity.ok("회원정보가 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패: 유효하지 않은 토큰입니다.");
        }
    }


    /**
     * @회원 정보 삭제
     *
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        if(customUserDetails == null){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 없습니다.");
        }

        Long authenticatedUserId = customUserDetails.getId();
        if(!authenticatedUserId.equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        userService.DeleteUser(userId);

        return ResponseEntity.ok("회원이 성공적으로 삭제 되었습니다.");


    }
}
