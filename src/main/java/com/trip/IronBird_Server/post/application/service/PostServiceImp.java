package com.trip.IronBird_Server.post.application.service;

import com.trip.IronBird_Server.plan.domain.Plan;
import com.trip.IronBird_Server.plan.infrastructure.PlanRepository;
import com.trip.IronBird_Server.post.adapter.dto.UploadImageDto;
import com.trip.IronBird_Server.post.adapter.mapper.PostMapper;
import com.trip.IronBird_Server.post.domain.Image;
import com.trip.IronBird_Server.post.domain.Post;
import com.trip.IronBird_Server.post.adapter.dto.PostDto;
import com.trip.IronBird_Server.post.infrastructure.ImageRepository;
import com.trip.IronBird_Server.post.infrastructure.PostRepository;
import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ImageRepository imageRepository;
    private final PostMapper postMapper;


    @Value ("${file.upload-dir}")
    private String uploadFolder;


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
    @Transactional
    public PostDto createPost(PostDto postDto,
                              UploadImageDto uploadImageDto,
                              String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(postDto.getTitle())
                .detail(postDto.getDetail())
                .user(user)
                .createTime(LocalDateTime.now())
                .modifyTime(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);

        //이미지 업로드 추가
        if(uploadImageDto.getImages() != null && !uploadImageDto.getImages().isEmpty()){
            for(MultipartFile file : uploadImageDto.getImages()){
                UUID uuid = UUID.randomUUID();
                String imagefileName = uuid +  "_" + file.getOriginalFilename();

                File destinationFile = new File(uploadFolder + imagefileName);

                try {
                    file.transferTo(destinationFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Image image = Image.builder()
                        .imageUrl("/boardImages/" + imagefileName)
                        .post(savedPost)
                        .build();

                imageRepository.save(image);
            }

        }

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


    /**
     * 게시물 삭제
     * @param
     * @param id
     */
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
