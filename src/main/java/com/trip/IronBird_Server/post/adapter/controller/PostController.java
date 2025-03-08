package com.trip.IronBird_Server.post.adapter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.IronBird_Server.common.custom.CustomUserDetails;
import com.trip.IronBird_Server.post.adapter.dto.PostDto;
import com.trip.IronBird_Server.post.adapter.dto.UploadImageDto;
import com.trip.IronBird_Server.post.application.service.PostServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImp postServiceImp;

    /**
     * 등록된 포스팅 전체 가져오는 컨트롤러
     */

    @GetMapping
    public List<PostDto> getAllPost(){

        return postServiceImp.getAllPosts();
    }


    /**
     * 포스팅 생성 컨트롤러
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(@RequestParam("postDto") String postDtoJson,
                                              @RequestParam("images") List<MultipartFile> images,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }
        String email = userDetails.getEmail();

        // JSON 문자열을 PostDto 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        PostDto postDto;
        try {
            postDto = objectMapper.readValue(postDtoJson, PostDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }

        UploadImageDto uploadImageDto = new UploadImageDto();
        uploadImageDto.setImages(images);

        PostDto createdPost = postServiceImp.createPost(postDto, uploadImageDto, email);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }








    /**
     * 포스팅 수정 컨트롤러
     */
    @PutMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@RequestBody PostDto postDto,
                                        @PathVariable("postId") Long postId){
        PostDto updateDto = postServiceImp.updatePost(postId, postDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(updateDto);
    }

    /**
     * 포스팅 삭제 컨트롤러
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId){
        try{
            postServiceImp.deletePost(postId);

            return ResponseEntity.ok("게시물이 삭제되었습니다.");
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.CREATED).body("게시물이 삭제되었습니다.");
        }

    }



}
