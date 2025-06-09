package com.ssafy.ssafy_13ban_archive.group.model.entity;

public enum Status {
    PENDING("대기"),
    ACCEPTED("수락됨"),;

    private final String description;

    Status(String description) {
        this.description = description;
    }
}
