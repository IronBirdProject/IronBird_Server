package com.trip.IronBird_Server.post.service;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.repository.PlanRepository;
import com.trip.IronBird_Server.post.domain.Post;
import com.trip.IronBird_Server.post.dto.PostDto;
import com.trip.IronBird_Server.post.repository.PostRepository;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;


    public List<PostDto> getAllPosts(){
        return postRepository.findAll().stream()
                .map(post -> PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .detail(post.getDetail())
                        .userId(post.getUser().getId())
                        .planId(post.getPlan() != null ? post.getPlan().getId() : null) // Plan 이 null 이라면 null 반환
                        .createTime(post.getCreateTime())
                        .modifyTime(post.getModifyTime())
                        .build())
                .collect(Collectors.toList());
    }


    public PostDto createPost(PostDto postDto, String email){
        //사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //Plan 조회
        Plan plan = null;

        if (postDto.getPlanId() != null){
            Optional<Plan> optionalPlan = planRepository.findById(postDto.getPlanId());
            if (optionalPlan.isPresent()){
                plan = optionalPlan.get();  //플랜이 있다면 가져와서 할당
            }else {
                log.warn("경고 : 해당 플랜을 찾을 수 없습니다. 플랜 없이 게시물을 생성합니다.");
            }
        }else {
            log.warn("알림 : 플랜ID가 제공되지 않았습니다. 플랜 없이 게시물을 생성합니다.");
        }

        // Post 엔티티에 생성 및 저장
        Post post = Post.builder()
                .title(postDto.getTitle())
                .detail(postDto.getDetail())
                .user(user)
                .plan(plan)
                .createTime(LocalDateTime.now())
                .modifyTime(LocalDateTime.now())
                .build();

        postRepository.save(post);

        return PostDto.builder()
                .id(post.getId())
                .title(postDto.getTitle())
                .detail(postDto.getDetail())
                .userId(user.getId())
                .planId(plan != null ? plan.getId() : null)
                .createTime(post.getCreateTime())
                .modifyTime(post.getModifyTime())
                .build();
    }

}
