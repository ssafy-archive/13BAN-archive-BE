package com.ssafy.ssafy_13ban_archive.security.dto;

import lombok.Data;

@Data
public class JwtUserInfo {

    private final int userId;
    private final String loginId;
    private final String name;
    private final String ssafyNumber;
    private final String role;

    /**
     * 관리자 여부 확인
     */
    public boolean isAdmin() {
        return role.equals("ROLE_ADMIN");
    }

    /**
     * 일반 사용자 여부 확인
     */
    public boolean isUser() {
        return role.equals("ROLE_USER");
    }

}
