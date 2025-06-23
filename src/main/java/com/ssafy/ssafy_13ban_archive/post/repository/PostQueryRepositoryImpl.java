package com.ssafy.ssafy_13ban_archive.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Post;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostSubCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.QPost;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllByConditionWithLastPostId(PostCategory category, PostSubCategory subCategory, Integer groupId, Integer lastPostId, Integer size, String title, Integer userId) {
        QPost qPost = QPost.post;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (category != null) {
            booleanBuilder.and(qPost.category.eq(category));
        }
        if (subCategory != null) {
            booleanBuilder.and(qPost.subCategory.eq(subCategory));
        }
        if (groupId != null) {
            booleanBuilder.and(qPost.groupId.eq(groupId));
        }
        if (title != null && !title.isEmpty()) {
            booleanBuilder.and(qPost.title.contains(title));
        }
        if (userId != null) {
            booleanBuilder.and(qPost.user.userId.eq(userId));
        }
        if (lastPostId != null) {
            booleanBuilder.and(qPost.postId.gt(lastPostId));
        }

        return jpaQueryFactory
                .selectFrom(qPost)
                .where(booleanBuilder)
                .orderBy(qPost.postId.desc())
                .limit(size)
                .fetch();

    }
}
