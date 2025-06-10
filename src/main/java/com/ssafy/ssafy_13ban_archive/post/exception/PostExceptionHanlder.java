package com.ssafy.ssafy_13ban_archive.post.exception;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.ErrorBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class PostExceptionHanlder {
    @ExceptionHandler(FileNotUploadedException.class)
    public CommonResponse<ErrorBody> fileNotUploadedException(FileNotUploadedException e, HttpServletRequest request) {
        log.warn("POST-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("POST-001", "파일 업로드에 실패했습니다."),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public CommonResponse<ErrorBody> invalidFileTypeException(InvalidFileTypeException e, HttpServletRequest request) {
        log.warn("POST-002> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("POST-002", "유효하지 않은 파일 형식입니다."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public CommonResponse<ErrorBody> postNotFoundException(PostNotFoundException e, HttpServletRequest request) {
        log.warn("POST-003> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("POST-003", "해당 ID의 게시글을 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }
}
