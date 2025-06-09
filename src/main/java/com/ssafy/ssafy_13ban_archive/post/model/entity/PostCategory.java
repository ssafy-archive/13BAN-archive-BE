package com.ssafy.ssafy_13ban_archive.post.model.entity;

public enum PostCategory {
    INFORMATRION("정보 공유"),
    GALLERY("갤러리"),
    FREE("자유 게시판"),;

    private final String description;

    PostCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
