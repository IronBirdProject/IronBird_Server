package com.trip.IronBird_Server.post.controller;

import com.trip.IronBird_Server.post.dto.PostDto;
import com.trip.IronBird_Server.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;



    @GetMapping
    public List<PostDto> getAllPost(){

        return postService.getAllPosts();
    }


    /**
     * 포스팅 생성 컨트롤러
     */
    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }
        String email = userDetails.getUsername();  // 이메일 가져오기
        PostDto createdPost = postService.createPost(postDto, email);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }



}
