package com.ssafy.ssafy_13ban_archive.user.model.entity;

import com.ssafy.ssafy_13ban_archive.common.model.entity.DateEntity;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Comment;
import com.ssafy.ssafy_13ban_archive.post.model.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookMark> bookmarks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupUser> groupUsers;

}
