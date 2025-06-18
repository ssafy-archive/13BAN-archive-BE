package com.ssafy.ssafy_13ban_archive.post.service;

import com.ssafy.ssafy_13ban_archive.post.model.entity.Comment;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Post;
import com.ssafy.ssafy_13ban_archive.post.model.request.CommentRequestDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.CommentResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.repository.CommentRepository;
import com.ssafy.ssafy_13ban_archive.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 생성
     */
    @Transactional
    public CommentResponseDTO createComment(Integer postId, Integer userId, CommentRequestDTO request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(request.getContent())
                .build();

        Comment saved = commentRepository.save(comment);
        return toResponseDTO(saved);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponseDTO updateComment(Integer postId, Integer commentId, Integer userId, CommentRequestDTO request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("게시글에 해당하는 댓글이 아닙니다.");
        }

        if (!comment.getUserId().equals(userId)) {
            throw new SecurityException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        comment.setContent(request.getContent());
        return toResponseDTO(comment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public boolean deleteComment(Integer postId, Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("게시글에 해당하는 댓글이 아닙니다.");
        }

        if (!comment.getUserId().equals(userId)) {
            throw new SecurityException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
        return true;
    }

    /**
     * 응답 DTO로 변환
     */
    private CommentResponseDTO toResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(null) // 혹시 createdAt 필드가 없을 수도 있으니 일단 null 처리
                .updatedAt(null)
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .build();
    }
}