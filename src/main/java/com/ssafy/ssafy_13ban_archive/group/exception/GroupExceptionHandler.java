package com.ssafy.ssafy_13ban_archive.group.exception;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.ErrorBody;
import com.ssafy.ssafy_13ban_archive.post.exception.FileNotUploadedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GroupExceptionHandler {
    @ExceptionHandler(KeyNotCreatedException.class)
    public CommonResponse<ErrorBody> keyNotCreatedException(KeyNotCreatedException e, HttpServletRequest request) {
        log.warn("GROUP-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-001", "그룹 생성에 실패했습니다."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
