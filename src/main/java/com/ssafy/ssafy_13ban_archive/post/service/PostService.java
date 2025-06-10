package com.ssafy.ssafy_13ban_archive.post.service;

import com.ssafy.ssafy_13ban_archive.post.exception.PostNotFoundException;
import com.ssafy.ssafy_13ban_archive.post.model.entity.File;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Image;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Post;
import com.ssafy.ssafy_13ban_archive.post.model.request.PostRequestDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.FileResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.ImageResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.PostResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    public PostResponseDTO getPostById(Integer postId) {
        // 해당 post가 존재하는지 확인
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("글을 찾을 수 없습니다.");
        }

        // post 조회
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("글을 찾을 수 없습니다.")
        );

        // 이미지와 파일 응답 DTO 리스트 생성
        List<Image> images = post.getImages();
        List<File> files = post.getFiles();
        List<ImageResponseDTO> imageResponseDTOs = images.stream()
                .map(this::convertToImageResponseDTO)
                .toList();
        List<FileResponseDTO> fileResponseDTOs = files.stream()
                .map(this::convertToFileResponseDTO)
                .toList();

        // PostResponseDTO 생성
        return convertToPostResponse(post, imageResponseDTOs, fileResponseDTOs);
    }

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
            imageResponseDTOs = fileService.uploadImages(
                    images,
                    post.getPostId(),
                    postRequestDTO.getImageComments()
            );
        }

        // PostResponseDTO 생성
        return convertToPostResponse(post, imageResponseDTOs, fileResponseDTOs);
    }

    @Transactional
    public boolean deletePost(Integer postId) {
        // 해당 post가 존재하는지 확인
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("글을 찾을 수 없습니다.");
        }

        // 파일과 이미지 삭제
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("글을 찾을 수 없습니다.")
        );

        List<File> files = post.getFiles();
        fileService.deleteFiles(files);

        List<Image> images = post.getImages();
        fileService.deleteImages(images);

        // post 삭제
        postRepository.deleteById(postId);
        return true; // 성공적으로 삭제된 경우 true 반환

    }

    ImageResponseDTO convertToImageResponseDTO(Image image) {
        return new ImageResponseDTO(
                image.getImageId(),
                fileService.getImageUrl(image.getImageLink()),
                image.getComment()
        );
    }

    FileResponseDTO convertToFileResponseDTO(File file) {
        return new FileResponseDTO(
                file.getFileId(),
                file.getFileLink()
        );
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
