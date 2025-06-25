package com.ssafy.ssafy_13ban_archive.post.repository;

import com.ssafy.ssafy_13ban_archive.post.model.entity.Post;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostSubCategory;

import java.util.List;

public interface PostQueryRepository {
    List<Post> findAllByConditionWithLastPostId(
            PostCategory category,
            PostSubCategory subCategory,
            Integer groupId, Integer lastPostId, Integer size, String title, Integer userId);
}
