package com.trip.IronBird_Server.post.application.service;

import com.trip.IronBird_Server.post.adapter.dto.PostDto;

import java.util.List;

public interface PostService {

    public List<PostDto> getAllPosts();
    public PostDto createPost(PostDto postDto, String email);
    public PostDto updatePost(Long id, PostDto postDto);
    public void deletePost(Long id);
    public void incrementLike(Long postId);

}
