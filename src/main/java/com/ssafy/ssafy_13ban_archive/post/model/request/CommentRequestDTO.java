package com.ssafy.ssafy_13ban_archive.post.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    @Schema(description = "댓글 내용", example = "이 게시글 정말 유익하네요!")
    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    private String content;

}
