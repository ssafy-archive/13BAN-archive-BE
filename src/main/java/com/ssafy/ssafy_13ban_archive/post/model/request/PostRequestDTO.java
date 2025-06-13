package com.ssafy.ssafy_13ban_archive.post.model.request;

import com.ssafy.ssafy_13ban_archive.post.model.entity.PostCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostSubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDTO {
    private String title;
    private String content;
    private Integer groupId;
    private PostCategory postCategory;
    private PostSubCategory postSubCategory;
    private Map<String, String> imageComments; // imagename -> comment
}
