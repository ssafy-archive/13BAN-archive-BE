package com.ssafy.ssafy_13ban_archive.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;
import com.ssafy.ssafy_13ban_archive.group.model.entity.QGroup;
import com.ssafy.ssafy_13ban_archive.group.model.entity.QGroupUser;
import com.ssafy.ssafy_13ban_archive.user.model.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GroupQueryRepositoryImpl implements GroupQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<GroupUser> findGroupUsersByGroupId(Integer groupId) {
        QGroupUser gu = QGroupUser.groupUser;
        QUser u = QUser.user;

        return queryFactory
                .selectFrom(gu)
                .join(gu.user, u).fetchJoin()
                .where(gu.group.groupId.eq(groupId))
                .fetch();
    }
}
