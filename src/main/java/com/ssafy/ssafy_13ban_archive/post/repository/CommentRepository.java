package com.ssafy.ssafy_13ban_archive.post.repository;

import com.ssafy.ssafy_13ban_archive.post.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // 특정 게시글(postId)에 달린 모든 댓글 조회 (최신순 정렬 등은 서비스단에서 처리 가능)
    List<Comment> findByPostId(Integer postId);

    // (선택) 특정 유저가 쓴 댓글 모두 조회
    List<Comment> findByUserId(Integer userId);

}