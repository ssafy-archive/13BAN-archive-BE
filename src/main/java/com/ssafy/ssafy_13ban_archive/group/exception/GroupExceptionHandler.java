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

    @ExceptionHandler(NotInGroupException.class)
    public CommonResponse<ErrorBody> notInGroupException(NotInGroupException e, HttpServletRequest request) {
        log.warn("GROUP-002> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-002", "그룹에 속해 있지 않습니다."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAdminException.class)
    public CommonResponse<ErrorBody> notAdminException(NotAdminException e, HttpServletRequest request) {
        log.warn("GROUP-003> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-003", "그룹 관리자만 접근할 수 있습니다."),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotCreatorException.class)
    public CommonResponse<ErrorBody> notCreatorException(NotCreatorException e, HttpServletRequest request) {
        log.warn("GROUP-004> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-004", "그룹 생성자만 접근할 수 있습니다."),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CreatorLeaveException.class)
    public CommonResponse<ErrorBody> creatorLeaveException(CreatorLeaveException e, HttpServletRequest request) {
        log.warn("GROUP-005> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-005", "그룹 생성자는 그룹을 떠날 수 없습니다."),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public CommonResponse<ErrorBody> groupNotFoundException(GroupNotFoundException e, HttpServletRequest request) {
        log.warn("GROUP-006> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-006", "그룹을 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GroupUserExistException.class)
    public CommonResponse<ErrorBody> groupUserExistException(GroupUserExistException e, HttpServletRequest request) {
        log.warn("GROUP-007> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-007", "이미 그룹에 속해 있습니다."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public CommonResponse<ErrorBody> userNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.warn("GROUP-008> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("GROUP-008", "사용자를 찾을 수 없습니다."),
                HttpStatus.NOT_FOUND);
    }
}
