package com.ssafy.ssafy_13ban_archive.group.exception;

public class GroupUserExistException extends RuntimeException {
    public GroupUserExistException(String message) {
        super(message);
    }
}
