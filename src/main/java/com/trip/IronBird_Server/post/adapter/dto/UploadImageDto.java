package com.trip.IronBird_Server.post.adapter.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadImageDto {

    private List<MultipartFile> images;

}
