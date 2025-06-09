package com.ssafy.ssafy_13ban_archive.group.model.entity;

import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "group_user")
public class GroupUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_user_id")
    private Integer groupUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_role", nullable = false)
    private GroupRole groupRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
