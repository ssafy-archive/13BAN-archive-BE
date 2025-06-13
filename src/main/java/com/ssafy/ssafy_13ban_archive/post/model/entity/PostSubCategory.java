package com.ssafy.ssafy_13ban_archive.post.model.entity;

import lombok.Getter;

@Getter
public enum PostSubCategory {
    // 정보 공유
    STUDY(PostCategory.INFORMATRION, "공부"),
    INTERVIEW_REVIEW(PostCategory.INFORMATRION, "면접 후기"),
    CODING_TEST_REVIEW(PostCategory.INFORMATRION, "코딩 테스트 후기"),
    OTHER(PostCategory.INFORMATRION, "기타"),
    // 갤러리
    //자유게시판
    NONE(null, null),
    ;

    private final PostCategory postCategory;
    private final String description;

    PostSubCategory(PostCategory postCategory, String description) {
        this.postCategory = postCategory;
        this.description = description;
    }
}
