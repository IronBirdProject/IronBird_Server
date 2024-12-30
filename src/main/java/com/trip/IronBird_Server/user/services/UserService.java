package com.trip.IronBird_Server.user.services;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import com.trip.IronBird_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    // 회원가입
    public User registerUser(String email, String password, String name, String defaultProfilePic){
        // 이메일 중복 체크
        if(userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 사용자 저장
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .oauthType(OauthType.NONE)
                .profilePic(defaultProfilePic)
                .build();

        return userRepository.save(user);
    }

    //로그인
    public TokenDto login(String email, String password){

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 이메일 입니다."));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        // 사용자 권한 및 정보 설정
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub",user.getEmail());
        claims.put("role","ROLE_USER");

        return tokenProvider.generateTokenDto(claims);
    }
}
