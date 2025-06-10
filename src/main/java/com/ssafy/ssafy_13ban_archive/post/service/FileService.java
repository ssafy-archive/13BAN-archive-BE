package com.ssafy.ssafy_13ban_archive.post.service;

import com.ssafy.ssafy_13ban_archive.common.util.S3Util;
import com.ssafy.ssafy_13ban_archive.post.exception.FileNotUploadedException;
import com.ssafy.ssafy_13ban_archive.post.exception.InvalidFileTypeException;
import com.ssafy.ssafy_13ban_archive.post.model.entity.File;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Image;
import com.ssafy.ssafy_13ban_archive.post.model.response.FileResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.ImageResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.repository.FileRepository;
import com.ssafy.ssafy_13ban_archive.post.repository.ImageRepository;
import io.awspring.cloud.s3.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Util s3Util;
    private final FileRepository fileRepository;
    private final ImageRepository imageRepository;

    List<FileResponseDTO> uploadFiles(List<MultipartFile> files, Integer postId) {
        List<FileResponseDTO> fileResponseDTOs = new ArrayList<>();
        try {
            for(MultipartFile file : files){
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    throw new InvalidFileTypeException("파일명이 비었습니다.");
                }

                String s3Key = generateS3Key(postId, originalFilename, "file");
                ObjectMetadata objectMetadata = ObjectMetadata.builder()
                        .contentType(file.getContentType())
                        .build();
                boolean uploadSuccess = s3Util.uploadFile(s3Key, file.getInputStream(), objectMetadata);

                if (uploadSuccess) {
                    File uploadedFile = File.builder()
                            .fileLink(s3Key)
                            .build();
                    uploadedFile.setPostId(postId);
                    fileRepository.save(uploadedFile);
                    fileResponseDTOs.add(new FileResponseDTO(uploadedFile.getFileId(), s3Util.getFileUrl(s3Key).toString()));
                }else throw new FileNotUploadedException("파일 업로드에 실패했습니다.");
            }
            return fileResponseDTOs;
        } catch (FileNotUploadedException | IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            // 파일 업로드 실패 시, 업로드된 파일과 이미지를 삭제하고 예외를 던짐
            for(FileResponseDTO fileResponseDTO : fileResponseDTOs) {
                s3Util.deleteFile(fileResponseDTO.getFileLink());
                fileRepository.deleteById(fileResponseDTO.getFileId());
            }
            throw new FileNotUploadedException("파일 업로드에 실패했습니다.");
        }
    }

    List<ImageResponseDTO> uploadImages(List<MultipartFile> images, Integer postId) {
        List<ImageResponseDTO> imageResponseDTOs = new ArrayList<>();
        try {
            for(MultipartFile image : images){
                String originalFilename = image.getOriginalFilename();
                if (!isValidImage(image)) {
                    throw new InvalidFileTypeException("이미지 형식이 아닙니다.");
                }

                String s3Key = generateS3Key(postId, originalFilename, "image");
                ObjectMetadata objectMetadata = ObjectMetadata.builder()
                        .contentType(image.getContentType())
                        .build();
                boolean uploadSuccess = s3Util.uploadFile(s3Key, image.getInputStream(), objectMetadata);

                if (uploadSuccess) {
                    Image uploadedFile = Image.builder()
                            .imageLink(s3Key)
                            .comment("")
                            .build();
                    uploadedFile.setPostId(postId);
                    imageRepository.save(uploadedFile);
                    imageResponseDTOs.add(new ImageResponseDTO(uploadedFile.getImageId(), s3Util.getFileUrl(s3Key).toString(), ""));
                }else throw new FileNotUploadedException("파일 업로드에 실패했습니다.");
            }
            return imageResponseDTOs;
        } catch (FileNotUploadedException | IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
            // 파일 업로드 실패 시, 업로드된 파일과 이미지를 삭제하고 예외를 던짐
            for(ImageResponseDTO imageResponseDTO : imageResponseDTOs) {
                s3Util.deleteFile(imageResponseDTO.getImageLink());
                imageRepository.deleteById(imageResponseDTO.getImageId());
            }
            throw new FileNotUploadedException("파일 업로드에 실패했습니다.");
        } catch (InvalidFileTypeException e) {
            log.error("잘못된 파일 형식: {}", e.getMessage());
            throw e; // 예외를 다시 던져서 호출자에게 알림
        }
    }

    public boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return false;

        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();

        return hasAllowedExtension(filename) && isImageMimeType(contentType);
    }

    private boolean hasAllowedExtension(String filename) {
        if (filename == null) return false;

        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".webp");
    }

    private boolean isImageMimeType(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    private String generateS3Key(Integer postId, String originalFilename, String type) {
        String uuid = UUID.randomUUID().toString();
        String encodedName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);

        return String.format("post-%d/%s/%s/%s", postId, type, uuid, encodedName);
    }

}
