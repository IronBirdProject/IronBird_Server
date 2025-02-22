package com.trip.IronBird_Server.post.application.service;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.infrastructure.PlanRepository;
import com.trip.IronBird_Server.post.adapter.mapper.PostMapper;
import com.trip.IronBird_Server.post.domain.Post;
import com.trip.IronBird_Server.post.adapter.dto.PostDto;
import com.trip.IronBird_Server.post.infrastructure.PostRepository;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PostMapper postMapper;


    /**
     * 모든 게시물 조회
     * @return
     */
    @Override
    public List<PostDto> getAllPosts(){
        return postRepository.findAll().stream()
                .map(postMapper::postDto)
                .collect(Collectors.toList());
    }


    /**
     * 게시물 생성
     * @param postDto
     * @param email
     * @return
     */
    @Override
    public PostDto createPost(PostDto postDto, String email) {
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // Plan 조회
        Plan plan = null;
        if (postDto.getPlanId() != null) {
            plan = planRepository.findById(postDto.getPlanId())
                    .orElseThrow(() -> new IllegalArgumentException("Plan ID: " + postDto.getPlanId() + "를 찾을 수 없습니다."));
        } else {
            log.warn("Plan ID가 제공되지 않았습니다. Plan 없이 게시물을 생성합니다.");
        }

        LocalDateTime now = LocalDateTime.now();

        // Post 엔티티 생성 및 저장
        Post post = Post.builder()
                .title(postDto.getTitle())
                .detail(postDto.getDetail())
                .user(user)
                .plan(plan)
                .createTime(now)
                .modifyTime(now)
                .build();

        Post savedPost = postRepository.save(post);
        log.debug("Saved Post Plan ID: {}", savedPost.getPlan() != null ? savedPost.getPlan().getId() : "NULL");

        return postMapper.postDto(savedPost);
    }


    /**
     * 게시물 수정
     * @param id
     * @param postDto
     * @return
     */
    @Transactional
    @Override
    public PostDto updatePost(Long id, PostDto postDto){

        //기존 게시물 조회
        Post exitPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post Not Found"));


        exitPost.setTitle(postDto.getTitle());
        exitPost.setDetail(postDto.getDetail());
        exitPost.setModifyTime(LocalDateTime.now());

        //수정된 데이터 저장
        Post updatePost = postRepository.save(exitPost);

        // 저장된 결과를 PostDto로 반환
        return postMapper.postDto(updatePost);
    }


    //게시물 삭제
    @Override
    public void deletePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("게시물을 찾을 수 없습니다."));

        postRepository.delete(post);

    }


    /**
     * 좋아요 관리
     */

    //좋아요 증가
    @Override
    public void incrementLike(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("Post not found"));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }



}
