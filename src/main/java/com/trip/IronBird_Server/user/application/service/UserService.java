package com.trip.IronBird_Server.user.application.service;

import com.trip.IronBird_Server.user.adapter.dto.UserDto;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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


    //회원정보 수정
    public User updateUser(Long userId, UserDto userDto) {
        log.info("Updating user with ID: {}", userId);

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for ID: {}", userId);
                    return new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.");
                });

        // 이메일 중복 체크
        if (userDto.getEmail() != null &&
                !userDto.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(userDto.getEmail())) {
            log.warn("Email conflict for userId {}: {}", userId, userDto.getEmail());
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 사용자 정보 업데이트
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getDefaultProfilePic() != null) user.setProfilePic(userDto.getDefaultProfilePic());

        log.info("Updated user: {}", user);

        // 저장
        return userRepository.save(user);
    }

    public void DeleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        userRepository.delete(user);


    }

}
