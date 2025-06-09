package com.ssafy.ssafy_13ban_archive.group.model.entity;

public enum GroupRole {
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    GroupRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
