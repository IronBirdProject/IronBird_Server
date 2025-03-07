package com.trip.IronBird_Server.post.application.service;

import com.trip.IronBird_Server.post.adapter.dto.PostDto;
import com.trip.IronBird_Server.post.adapter.dto.UploadImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    public List<PostDto> getAllPosts();
    public PostDto createPost(PostDto postDto, UploadImageDto uploadImageDto, String email);
    public PostDto updatePost(Long id, PostDto postDto);
    public void deletePost(Long id);
    public void incrementLike(Long postId);

}
