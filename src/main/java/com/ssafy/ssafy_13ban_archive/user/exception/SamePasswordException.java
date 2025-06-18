package com.ssafy.ssafy_13ban_archive.user.exception;

public class SamePasswordException extends RuntimeException {
    public SamePasswordException(String message) { super(message); }
}
