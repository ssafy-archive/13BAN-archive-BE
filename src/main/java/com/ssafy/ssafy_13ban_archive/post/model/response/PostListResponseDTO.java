package com.ssafy.ssafy_13ban_archive.post.model.response;

import com.ssafy.ssafy_13ban_archive.post.model.entity.PostCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostSubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostListResponseDTO {
    private PostCategory category;
    private PostSubCategory subCategory;
    private Integer groupId;
    private Integer LastPostId;
    private Integer size;
    private List<SimplePostResponseDTO> posts;
}
