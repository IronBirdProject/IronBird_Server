package com.trip.IronBird_Server.post.domain;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "detail")
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column(name = "uploadTime")
    private LocalDateTime createTime;

    @Column(name = "modification_Time")
    private LocalDateTime modifyTime;

    @Column(name = "liked_count")
    private Long likeCount = 0L;    // Default = 0;

    @OneToMany
    private List<Image> images = new ArrayList<>();  // 이미지 업로드

}
