package com.trip.IronBird_Server.user.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    private String email;
    private String password;
    private String name;    //이름 & 닉네임
    private String defaultProfilePic;
}
