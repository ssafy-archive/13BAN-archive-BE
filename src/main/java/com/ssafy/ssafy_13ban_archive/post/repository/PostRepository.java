package com.ssafy.ssafy_13ban_archive.post.repository;

import com.ssafy.ssafy_13ban_archive.post.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer>, QuerydslPredicateExecutor<Post> {
}
