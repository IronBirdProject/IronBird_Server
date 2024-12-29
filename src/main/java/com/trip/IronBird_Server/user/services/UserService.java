package com.trip.IronBird_Server.user.services;

import com.trip.IronBird_Server.jwt.TokenProvider;
import com.trip.IronBird_Server.jwt.dto.TokenDto;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import com.trip.IronBird_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
