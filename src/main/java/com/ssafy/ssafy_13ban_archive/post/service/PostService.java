package com.ssafy.ssafy_13ban_archive.post.service;

import com.ssafy.ssafy_13ban_archive.common.util.S3Util;
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

    private final S3Util s3Util;
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public PostResponseDTO createPost(
            PostRequestDTO postRequestDTO,
            List<MultipartFile> images,
            List<MultipartFile> files
    ) {

        try{
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
            List<FileResponseDTO> fileResponseDTOs = new ArrayList<>();
            if(files != null){
                for(MultipartFile file : files){
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        continue; // Skip files without a valid name
                    }

                    String s3Key = generateS3Key(post.getPostId(), originalFilename, "file");
                    ObjectMetadata objectMetadata = ObjectMetadata.builder()
                            .contentType(file.getContentType())
                            .build();
                    boolean uploadSuccess = s3Util.uploadFile(s3Key, file.getInputStream(), objectMetadata);

                    if (uploadSuccess) {
                        File uploadedFile = File.builder()
                                .fileLink(s3Key)
                                .build();
                        uploadedFile.setPostId(post.getPostId());
                        fileRepository.save(uploadedFile);
                        fileResponseDTOs.add(new FileResponseDTO(uploadedFile.getFileId(), s3Util.getFileUrl(s3Key).toString()));
                    }
                }
            }

            // 이미지 업로드
            List<ImageResponseDTO> imageResponseDTOs = new ArrayList<>();
            if(images != null) {
                for(MultipartFile image : images){
                    String originalFilename = image.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        continue; // Skip files without a valid name
                    }

                    String s3Key = generateS3Key(post.getPostId(), originalFilename, "image");
                    ObjectMetadata objectMetadata = ObjectMetadata.builder()
                            .contentType(image.getContentType())
                            .build();
                    boolean uploadSuccess = s3Util.uploadFile(s3Key, image.getInputStream(), objectMetadata);

                    if (uploadSuccess) {
                        Image uploadedFile = Image.builder()
                                .imageLink(s3Key)
                                .comment("")
                                .build();
                        uploadedFile.setPostId(post.getPostId());
                        imageRepository.save(uploadedFile);
                        imageResponseDTOs.add(new ImageResponseDTO(uploadedFile.getImageId(), s3Util.getFileUrl(s3Key).toString(), ""));
                    }
                }
            }

            // PostResponseDTO 생성
            return convertToPostResponse(post, imageResponseDTOs, fileResponseDTOs);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Post creation failed: " + e.getMessage());
        }
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

    private String generateS3Key(Integer postId, String originalFilename, String type) {
        String uuid = UUID.randomUUID().toString();
        String encodedName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);

        return String.format("post-%d/%s/%s/%s", postId, type, uuid, encodedName);
    }
}
