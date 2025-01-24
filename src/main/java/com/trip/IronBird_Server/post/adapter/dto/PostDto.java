package com.trip.IronBird_Server.post.adapter.dto;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private long id;
    private String title;
    private String detail;
    private Long userId;  //엔티티 대신 ID만 전달
    private Long planId;  //엔티티 대신 ID만 전달
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;



}
