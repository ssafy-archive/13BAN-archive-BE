package com.ssafy.ssafy_13ban_archive.group.service;

import com.ssafy.ssafy_13ban_archive.group.exception.*;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Group;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupRole;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Status;
import com.ssafy.ssafy_13ban_archive.group.model.request.GroupEnterRequestDto;
import com.ssafy.ssafy_13ban_archive.group.model.response.*;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupRepository;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupUserRepository;
import com.ssafy.ssafy_13ban_archive.group.util.GroupUtil;
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

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupUtil groupUtil;

    @Transactional
    public boolean leaveGroup(Integer groupId, JwtUserInfo jwtUserInfo) {
        User user = groupUtil.checkUserExists(jwtUserInfo);
        Group group = groupUtil.checkGroupExists(groupId);
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

    public MemberListResponseDTO getGroupMembers(Integer groupId, JwtUserInfo jwtUserInfo) {
        Group group = groupUtil.checkGroupExists(groupId);
        User user = groupUtil.checkUserExists(jwtUserInfo);

        // 그룹에 속해 있는지 확인
        groupUserRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new NotInGroupException("그룹에 속해 있지 않습니다."));

        List<GroupUser> groupUsers = groupRepository.findGroupUsersByGroupId(groupId);

        List<GroupMemberResponseDTO> memberList = groupUsers.stream()
                .map(groupUser -> {
                    User member = groupUser.getUser();
                    return new GroupMemberResponseDTO(
                            member.getUserId(),
                            member.getName(),
                            member.getSsafyNumber(),
                            groupUser.getGroupRole(),
                            groupUser.getStatus()
                    );
                }).toList();

        return new MemberListResponseDTO(group.getGroupId(), group.getGroupName(), memberList);
    }

    @Transactional
    public boolean requestJoinGroup(GroupEnterRequestDto groupEnterRequestDto, JwtUserInfo jwtUserInfo) {
        User user = groupUtil.checkUserExists(jwtUserInfo);
        Group group = groupRepository.findByGroupKey(groupEnterRequestDto.getGroupKey())
                .orElseThrow(() -> new GroupNotFoundException("그룹을 찾을 수 없습니다."));

        // 이미 그룹에 가입되어 있는지 확인
        if(groupUserRepository.findByGroupAndUser(group, user).isPresent()) {
            throw new GroupUserExistException("이미 그룹에 가입되어 있습니다.");
        }

        // 그룹 사용자 생성
        GroupUser groupUser = new GroupUser();
        groupUser.setGroup(group);
        groupUser.setUser(user);
        groupUser.setGroupRole(GroupRole.MEMBER);
        groupUser.setStatus(Status.PENDING);

        groupUserRepository.save(groupUser);
        return true;
    }
}
