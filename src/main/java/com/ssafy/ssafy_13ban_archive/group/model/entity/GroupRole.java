package com.ssafy.ssafy_13ban_archive.group.model.entity;

import lombok.Getter;

@Getter
public enum GroupRole {
    CREATOR("ROLE_CREATOR"),
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    GroupRole(String role) {
        this.role = role;
    }

}
