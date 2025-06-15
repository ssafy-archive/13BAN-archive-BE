package com.ssafy.ssafy_13ban_archive.user.exception;

public class SignInFailureException extends RuntimeException {
    public SignInFailureException(String message) {
        super(message);
    }
}
