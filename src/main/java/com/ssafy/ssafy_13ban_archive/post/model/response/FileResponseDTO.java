package com.ssafy.ssafy_13ban_archive.post.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FileResponseDTO {
    private Integer fileId;
    private String fileLink;
}
