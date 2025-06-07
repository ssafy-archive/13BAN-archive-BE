package com.ssafy.ssafy_13ban_archive.post.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Integer fileId;

    @Column(name="file_link", nullable = false)
    private String fileLink;

    @Column(name="post_id", nullable = false)
    private Integer postId;
}
