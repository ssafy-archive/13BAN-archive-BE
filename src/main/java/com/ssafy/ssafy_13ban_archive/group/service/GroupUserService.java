package com.ssafy.ssafy_13ban_archive.group.service;

import com.querydsl.core.BooleanBuilder;
import com.ssafy.ssafy_13ban_archive.group.exception.*;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Group;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupRole;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;
import com.ssafy.ssafy_13ban_archive.group.model.request.GroupCreateRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.GroupListResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.GroupResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.GroupRoleResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupRepository;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupUserRepository;
import com.ssafy.ssafy_13ban_archive.security.dto.JwtUserInfo;
import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import com.ssafy.ssafy_13ban_archive.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupUserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    @Transactional
    public boolean leaveGroup(Integer groupId, JwtUserInfo jwtUserInfo) {
        User user = checkUserExists(jwtUserInfo);
        Group group = checkGroupExists(groupId);
        GroupUser groupUser = groupUserRepository.findByGroupAndUser(group, user).orElseThrow(
                () -> new NotInGroupException("그룹 사용자 정보를 찾을 수 없습니다.")
        );
        if (groupUser.getGroupRole() == GroupRole.CREATOR) {
            throw new CreatorLeaveException("생성자는 그룹을 떠날 수 없습니다.");
        }
        groupUserRepository.delete(groupUser);
        return true;
    }

    public GroupListResponseDTO getMyGroups(JwtUserInfo jwtUserInfo) {
        List<GroupUser> groupUsers = groupUserRepository.findGroupsByUserId(jwtUserInfo.getUserId());

        List<GroupRoleResponseDTO> groupRoleResponseDTOs = groupUsers.stream()
                .map(groupUser -> {
                    Group group = groupUser.getGroup();
                    return new GroupRoleResponseDTO(
                            group.getGroupId(),
                            group.getGroupName(),
                            group.getGroupKey(),
                            groupUser.getGroupRole()
                    );
                }).toList();
        return new GroupListResponseDTO(groupRoleResponseDTOs);
    }

    private User checkUserExists(JwtUserInfo jwtUserInfo) {
        return userRepository.findById(jwtUserInfo.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Group checkGroupExists(Integer groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("그룹을 찾을 수 없습니다."));
    }
}
