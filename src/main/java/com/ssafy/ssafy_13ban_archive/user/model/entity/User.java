package com.ssafy.ssafy_13ban_archive.user.model.entity;

import com.ssafy.ssafy_13ban_archive.common.model.entity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name="name", nullable = false, length = 20)
    private String name;

    @Column(name="ssafy_number", nullable = false, length = 7)
    private String ssafyNumber;

    @Column(name="password", nullable = false, length = 60)
    private String password;

    @Column(name="login_id", nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(name="user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
