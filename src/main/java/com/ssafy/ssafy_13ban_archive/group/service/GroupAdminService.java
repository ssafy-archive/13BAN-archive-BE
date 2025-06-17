package com.ssafy.ssafy_13ban_archive.group.service;

import com.ssafy.ssafy_13ban_archive.group.exception.NotAdminException;
import com.ssafy.ssafy_13ban_archive.group.exception.NotCreatorException;
import com.ssafy.ssafy_13ban_archive.group.exception.NotInGroupException;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Group;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupRole;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Status;
import com.ssafy.ssafy_13ban_archive.group.model.request.MemberAcceptRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.request.MemberRoleRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.request.RoleModifyRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.MemberListResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupRepository;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupUserRepository;
import com.ssafy.ssafy_13ban_archive.group.util.GroupUtil;
import com.ssafy.ssafy_13ban_archive.security.dto.JwtUserInfo;
import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupAdminService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupUserService groupUserService;
    private final GroupUtil groupUtil;

    @Transactional
    public MemberListResponseDTO modifyMemberRole(RoleModifyRequestDTO roleModifyRequestDTO, Integer groupId, JwtUserInfo jwtUserInfo) {
        GroupUser groupUser = groupUserRepository.findByGroupAndUser(
                Group.builder().groupId(groupId).build(),
                groupUtil.checkUserExists(jwtUserInfo)
        ).orElseThrow(() -> new NotInGroupException("그룹 사용자 정보를 찾을 수 없습니다."));

        if (groupUser.getGroupRole() != GroupRole.ADMIN) {
            throw new NotAdminException("그룹 관리자만 멤버 역할을 수정할 수 있습니다.");
        }

        List<MemberRoleRequestDTO> members = roleModifyRequestDTO.getMembers();

        for (MemberRoleRequestDTO member : members) {
            GroupUser targetGroupUser = groupUserRepository.findByGroupAndUser(
                    Group.builder().groupId(groupId).build(),
                    User.builder().userId(member.getUserId()).build()
            ).orElseThrow(() -> new NotInGroupException("수정하려는 그룹에 해당 사용자가 없습니다. userId : " + member.getUserId()));

            targetGroupUser.setGroupRole(member.getGroupRole());
            groupUserRepository.save(targetGroupUser);
        }
        return groupUserService.getGroupMembers(groupId, jwtUserInfo);
    }

    @Transactional
    public boolean acceptMemberRequest(MemberAcceptRequestDTO memberAcceptRequestDTO, Integer groupId, JwtUserInfo jwtUserInfo) {
        Group group = groupUtil.checkGroupExists(groupId);
        User user = groupUtil.checkUserExists(jwtUserInfo);

        GroupUser groupUser = groupUserRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new NotInGroupException("그룹 사용자 정보를 찾을 수 없습니다."));

        if (groupUser.getGroupRole() != GroupRole.ADMIN) {
            throw new NotAdminException("그룹 관리자만 멤버 요청을 수락할 수 있습니다.");
        }

        GroupUser requestedUser = groupUserRepository.findByGroupAndUser(
                group,
                User.builder().userId(memberAcceptRequestDTO.getUserId()).build()
        ).orElseThrow(() -> new NotInGroupException("신청하지 않은 사용자입니다."));

        if (requestedUser.getStatus() != Status.PENDING) {
            throw new NotInGroupException("멤버 요청이 아닙니다.");
        }

        requestedUser.setStatus(Status.ACCEPTED);
        groupUserRepository.save(requestedUser);
        return true;
    }
}
