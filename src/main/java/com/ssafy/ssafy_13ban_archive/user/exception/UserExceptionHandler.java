package com.ssafy.ssafy_13ban_archive.user.exception;

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
public class UserExceptionHandler {

    @ExceptionHandler(SignInFailureException.class)
    public CommonResponse<ErrorBody> signInFailureException(SignInFailureException e, HttpServletRequest request) {
        log.warn("USER-001> 요청 URI: " + request.getRequestURI() + ", 에러 메세지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-001", "회원가입에 실패했습니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public CommonResponse<ErrorBody> userNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.warn("USER-002> 요청 URI: " + request.getRequestURI() + ", 에러 메시지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-002", "잘못된 사용자입니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public CommonResponse<ErrorBody> passwordMismatchException(PasswordMismatchException e, HttpServletRequest request) {
        log.warn("USER-003> 요청 URI: " + request.getRequestURI() + ", 에러 메시지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-003", "현재 비밀번호가 일치하지 않습니다."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SamePasswordException.class)
    public CommonResponse<ErrorBody> samePasswordException(SamePasswordException e, HttpServletRequest request) {
        log.warn("USER-004> 요청 URI: " + request.getRequestURI() + ", 에러 메시지: " + e.getMessage());
        return new CommonResponse<>(new ErrorBody("USER-004", "새 비밀번호는 현재 비밀번호와 달라야 합니다."), HttpStatus.BAD_REQUEST);
    }

}
