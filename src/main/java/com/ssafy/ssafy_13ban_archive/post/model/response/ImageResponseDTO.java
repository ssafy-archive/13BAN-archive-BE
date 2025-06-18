package com.ssafy.ssafy_13ban_archive.post.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageResponseDTO {
    private Integer imageId;
    private String imageLink;
    private String comment;
}
