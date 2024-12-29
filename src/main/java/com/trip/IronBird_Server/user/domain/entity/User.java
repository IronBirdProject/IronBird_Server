package com.trip.IronBird_Server.user.domain.entity;

import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "`user`")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;    // 자체 서비스 회원용
    private String name;
    private String oauthId; // 소셜 로그인 사용자용 (소셜 제공자 ID)
    private String profilePic;

    @Enumerated(EnumType.STRING)
    private OauthType oauthType;    // 로그인 방식 구분


    public User createUser(String email, String password, String name, String defaultProfilePic){
        return User.builder()
                .email(email)
                .password(password) // 자체 회원 가입 시 비밀번호 설정
                .name(name)
                .oauthType(OauthType.NONE)  // 자체 회원
                .profilePic(defaultProfilePic)
                .build();
    }

    public User createSocialUser(String email, String oauthId,OauthType oauthType, String socialProfilePic){
        return User.builder()
                .email(email)
                .oauthId(oauthId)
                .oauthType(oauthType)
                .profilePic(socialProfilePic)
                .build();
    }

}
