package com.ssafy.ssafy_13ban_archive.post.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.SuccessResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.request.CommentRequestDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.CommentResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.service.CommentService;
import com.ssafy.ssafy_13ban_archive.security.dto.JwtUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공")
    })
    @PostMapping
    public CommonResponse<CommentResponseDTO> createComment(
            @PathVariable Integer postId,
            @RequestBody CommentRequestDTO request,
            @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        Integer userId = jwtUserInfo.getUserId(); // JwtUserInfo는 getUserId() 사용
        return new CommonResponse<>(
                commentService.createComment(postId, userId, request),
                HttpStatus.OK
        );
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    })
    @PutMapping("/{commentId}")
    public CommonResponse<CommentResponseDTO> updateComment(
            @PathVariable Integer postId,
            @PathVariable Integer commentId,
            @RequestBody CommentRequestDTO request,
            @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        Integer userId = jwtUserInfo.getUserId(); // 수정
        return new CommonResponse<>(
                commentService.updateComment(postId, commentId, userId, request),
                HttpStatus.OK
        );
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
    })
    @DeleteMapping("/{commentId}")
    public CommonResponse<SuccessResponseDTO> deleteComment(
            @PathVariable Integer postId,
            @PathVariable Integer commentId,
            @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        Integer userId = jwtUserInfo.getUserId(); // 수정
        boolean result = commentService.deleteComment(postId, commentId, userId);
        return new CommonResponse<>(new SuccessResponseDTO(result), HttpStatus.OK);
    }
}
