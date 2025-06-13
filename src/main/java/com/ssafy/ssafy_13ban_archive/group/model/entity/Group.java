package com.ssafy.ssafy_13ban_archive.group.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_group")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Column(name = "group_key", nullable = false, length = 50)
    private String groupKey;
}
