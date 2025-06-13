package com.ssafy.ssafy_13ban_archive.post.model.response;

import com.ssafy.ssafy_13ban_archive.post.model.entity.PostCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostSubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Integer postId;
    private Integer userId;
    private Integer groupId;
    private String title;
    private String content;

    private Integer viewCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostCategory postCategory;
    private PostSubCategory postSubCategory;

    private List<ImageResponseDTO> images;

    private List<FileResponseDTO> files;
}
