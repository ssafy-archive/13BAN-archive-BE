package com.ssafy.ssafy_13ban_archive.post.model.request;

import com.ssafy.ssafy_13ban_archive.post.model.entity.PostCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostSubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostModifyRequestDTO {
    private String title;
    private String content;
    private List<Integer> deleteImageIds; // 삭제할 이미지 ID 리스트
    private List<Integer> deleteFileIds; // 삭제할 파일 ID 리스트
    private Map<String,String> imageComments; // JSON 형태의 문자열로 받음
}
