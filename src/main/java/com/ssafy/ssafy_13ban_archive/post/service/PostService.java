package com.ssafy.ssafy_13ban_archive.post.service;

import com.ssafy.ssafy_13ban_archive.common.util.S3Util;
import com.ssafy.ssafy_13ban_archive.post.exception.FileNotUploadedException;
import com.ssafy.ssafy_13ban_archive.post.model.entity.File;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Image;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Post;
import com.ssafy.ssafy_13ban_archive.post.model.request.PostRequestDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.FileResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.ImageResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.PostResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.repository.FileRepository;
import com.ssafy.ssafy_13ban_archive.post.repository.ImageRepository;
import com.ssafy.ssafy_13ban_archive.post.repository.PostRepository;
import io.awspring.cloud.s3.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    @Transactional
    public PostResponseDTO createPost(
            PostRequestDTO postRequestDTO,
            List<MultipartFile> images,
            List<MultipartFile> files
    ) {

        // 파일과 이미지 응답 DTO 리스트 초기화
        List<FileResponseDTO> fileResponseDTOs = new ArrayList<>();
        List<ImageResponseDTO> imageResponseDTOs = new ArrayList<>();

        // post 생성
        Post post = Post.builder()
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .groupId(postRequestDTO.getGroupId())
                .userId(1) // 임시 유저아이디
                .viewCount(0)
                .category(postRequestDTO.getPostCategory())
                .subCategory(postRequestDTO.getPostSubCategory())
                .build();

        postRepository.save(post);

        // 파일 업로드
        if(files != null){
            fileResponseDTOs = fileService.uploadFiles(files, post.getPostId());
        }

        // 이미지 업로드
        if(images != null) {
            imageResponseDTOs = fileService.uploadImages(images, post.getPostId());
        }

        // PostResponseDTO 생성
        return convertToPostResponse(post, imageResponseDTOs, fileResponseDTOs);
    }

    private PostResponseDTO convertToPostResponse(Post post, List<ImageResponseDTO> imageResponseDTOs, List<FileResponseDTO> fileResponseDTOs) {
        return new PostResponseDTO(
                post.getPostId(),
                post.getUserId(),
                post.getGroupId(),
                post.getTitle(),
                post.getContent(),
                post.getViewCount(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getCategory(),
                post.getSubCategory(),
                imageResponseDTOs,
                fileResponseDTOs
        );
    }


}
