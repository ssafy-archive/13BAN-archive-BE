package com.ssafy.ssafy_13ban_archive.post.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name="image_link", nullable = false)
    private String imageLink;

    @Lob
    @Column(name="comment")
    private String comment;

    @Column(name="post_id", nullable = false)
    private Integer postId;
}
