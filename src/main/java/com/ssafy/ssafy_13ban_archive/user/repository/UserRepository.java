package com.ssafy.ssafy_13ban_archive.user.repository;

import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // loginId를 받아 DB 테이블에서 회원 조회
    User findByLoginId(String loginId);

}
