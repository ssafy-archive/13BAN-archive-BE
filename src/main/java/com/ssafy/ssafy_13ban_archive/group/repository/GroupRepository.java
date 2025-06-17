package com.ssafy.ssafy_13ban_archive.group.repository;

import com.ssafy.ssafy_13ban_archive.group.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Integer>, GroupQueryRepository{
    Optional<Group> findByGroupKey(String groupKey);
}
