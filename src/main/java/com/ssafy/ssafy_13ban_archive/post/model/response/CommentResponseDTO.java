package com.ssafy.ssafy_13ban_archive.post.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {

    private Integer commentId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer postId;

    private Integer userId;

}
