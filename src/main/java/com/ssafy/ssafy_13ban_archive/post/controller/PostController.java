package com.ssafy.ssafy_13ban_archive.post.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.SuccessResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.request.PostRequestDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.PostResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

     private final PostService postService;

    @PostMapping
    public CommonResponse<PostResponseDTO> createPost(
            @RequestPart("post") PostRequestDTO postRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return new CommonResponse<>(
                postService.createPost(postRequestDTO, images, files),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{postId}")
    public CommonResponse<PostResponseDTO> getPostById(@PathVariable Integer postId) {
        return new CommonResponse<>(postService.getPostById(postId), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public CommonResponse<SuccessResponseDTO> deletePost(@PathVariable Integer postId) {
        return new CommonResponse<>(new SuccessResponseDTO(postService.deletePost(postId)), HttpStatus.OK);
    }

}
