package com.trip.IronBird_Server.post.adapter.dto;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.post.domain.Image;
import com.trip.IronBird_Server.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private long id;
    private String title;
    private String detail;
    private String userName;  //ID 대신 사용자 이름
    private Long planId;  //엔티티 대신 ID만 전달
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private List<MultipartFile> uploadImage;

}
