package com.ssafy.ssafy_13ban_archive.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;
import com.ssafy.ssafy_13ban_archive.group.model.entity.QGroup;
import com.ssafy.ssafy_13ban_archive.group.model.entity.QGroupUser;
import com.ssafy.ssafy_13ban_archive.user.model.entity.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GroupUserQueryRepositoryImpl implements GroupUserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupUser> findGroupsByUserId(Integer userId) {
        QGroupUser gu = QGroupUser.groupUser;
        QGroup g = QGroup.group;
        QUser u = QUser.user;

        return queryFactory
                .selectFrom(gu)
                .join(gu.group, g).fetchJoin()
                .join(gu.user, u).fetchJoin()
                .where(u.userId.eq(userId))
                .fetch();
    }
}
