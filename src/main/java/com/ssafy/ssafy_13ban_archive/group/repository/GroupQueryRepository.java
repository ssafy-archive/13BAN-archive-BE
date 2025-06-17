package com.ssafy.ssafy_13ban_archive.group.repository;

import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;

import java.util.List;

public interface GroupQueryRepository {
    List<GroupUser> findGroupUsersByGroupId(Integer groupId);
}
