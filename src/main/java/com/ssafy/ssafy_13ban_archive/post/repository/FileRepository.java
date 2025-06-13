package com.ssafy.ssafy_13ban_archive.post.repository;

import com.ssafy.ssafy_13ban_archive.post.model.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
}
