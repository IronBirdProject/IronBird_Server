package com.trip.IronBird_Server.user.domain.entity;

import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String username;
//    private String password;

    private String name;

    private String oauthId;

    private OauthType oauthType;

}
