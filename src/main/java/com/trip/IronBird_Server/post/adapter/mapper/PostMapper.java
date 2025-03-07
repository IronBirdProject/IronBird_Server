package com.trip.IronBird_Server.post.adapter.mapper;

import com.trip.IronBird_Server.post.adapter.dto.PostDto;
import com.trip.IronBird_Server.post.domain.Image;
import com.trip.IronBird_Server.post.domain.Post;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PostMapper {

    public PostDto postDto(Post post){
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .detail(post.getDetail())
                .userName(post.getUser().getName())
                .planId(post.getPlan() != null ? post.getPlan().getId() : null)
                .createTime(post.getCreateTime())
                .modifyTime(post.getModifyTime())
                .uploadImage(post.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

}
