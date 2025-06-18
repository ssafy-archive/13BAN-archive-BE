package com.ssafy.ssafy_13ban_archive.common.util;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Util {
    private final S3Template s3Template;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public boolean uploadFile(String key, InputStream is, ObjectMetadata objectMetadata) {
        try {
            s3Template.upload(bucketName, key, is, objectMetadata);
            return true;
        } catch (Exception e) {
            log.error("S3 upload failed: {}", e.getMessage());
            return false;
        }
    }

    public URL getFileUrl(String key) {
        try {
            // 1 hour expiration for the signed URL
            return s3Template.createSignedGetURL(bucketName, key, Duration.ofMinutes(60));
        } catch (Exception e) {
            log.error("S3 get file URL failed: {}", e.getMessage());
            return null;
        }
    }

    public boolean checkFileExists(String key) {
        try {
            return s3Template.objectExists(bucketName, key);
        } catch (Exception e) {
            log.error("S3 check file existence failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean deleteFile(String key) {
        try {
            s3Template.deleteObject(bucketName, key);
            return true;
        } catch (Exception e) {
            log.error("S3 delete failed: {}", e.getMessage());
            return false;
        }
    }
}
