package com.ssafy.ssafy_13ban_archive.group.service;

import com.ssafy.ssafy_13ban_archive.group.exception.*;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Group;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupRole;
import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupUser;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Status;
import com.ssafy.ssafy_13ban_archive.group.model.request.GroupCreateRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.GroupResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupRepository;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupUserRepository;
import com.ssafy.ssafy_13ban_archive.security.dto.JwtUserInfo;
import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import com.ssafy.ssafy_13ban_archive.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupCRUDService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    @Transactional
    public GroupResponseDTO createGroup(GroupCreateRequestDTO groupCreateRequestDTO, JwtUserInfo jwtUserInfo) {
        Group group = new Group();
        group.setGroupName(groupCreateRequestDTO.getGroupName());
        for(int i = 0; i < 5; i++) {
            group.setGroupKey(UUID.randomUUID().toString());
            if(groupRepository.findByGroupKey(group.getGroupKey()).isEmpty()) { // Check if the group key is unique
                groupRepository.save(group);

                // 유저 정보 확인
                User user = checkUserExists(jwtUserInfo);

                // 이미 그룹에 가입되어 있는지 확인
                if(groupUserRepository.findByGroupAndUser(group, user).isPresent()) {
                    throw new GroupUserExistException("이미 그룹에 가입되어 있습니다.");
                }

                // 그룹 사용자 엔티티 생성 및 저장
                GroupUser groupUser = new GroupUser();
                groupUser.setGroup(group);
                groupUser.setUser(user);
                groupUser.setGroupRole(GroupRole.ADMIN);
                groupUser.setStatus(Status.ACCEPTED);
                groupUserRepository.save(groupUser);
                return convertToResponseDTO(group);
            }
        }
        throw new KeyNotCreatedException("키 생성에 실패했습니다.");
    }

    @Transactional
    public boolean modifyGroup(GroupCreateRequestDTO groupCreateRequestDTO, Integer groupId, JwtUserInfo jwtUserInfo) {
        User user = checkUserExists(jwtUserInfo);
        Group group = checkGroupExists(groupId);

        // 그룹 생성자만 그룹 이름을 수정할 수 있음
        GroupUser groupUser = groupUserRepository.findByGroupAndUser(group, user).orElseThrow(
                () -> new NotInGroupException("그룹 사용자 정보를 찾을 수 없습니다.")
        );
        if (groupUser.getGroupRole() != GroupRole.CREATOR) {
            throw new NotCreatorException("그룹 생성자만 그룹 이름을 수정할 수 있습니다.");
        }

        group.setGroupName(groupCreateRequestDTO.getGroupName());
        groupRepository.save(group);
        return true;
    }

    @Transactional
    public boolean deleteGroup(Integer groupId, JwtUserInfo jwtUserInfo) {
        User user = checkUserExists(jwtUserInfo);
        Group group = checkGroupExists(groupId);

        // 그룹 생성자만 그룹을 삭제할 수 있음
        GroupUser groupUser = groupUserRepository.findByGroupAndUser(group, user).orElseThrow(
                () -> new NotInGroupException("그룹 사용자 정보를 찾을 수 없습니다.")
        );
        if (groupUser.getGroupRole() != GroupRole.CREATOR) {
            throw new NotCreatorException("그룹 생성자만 그룹을 삭제할 수 있습니다.");
        }

        groupRepository.delete(group);
        return true;
    }

    private User checkUserExists(JwtUserInfo jwtUserInfo) {
        return userRepository.findById(jwtUserInfo.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private Group checkGroupExists(Integer groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("그룹을 찾을 수 없습니다."));
    }

    private GroupResponseDTO convertToResponseDTO(Group group) {
        return new GroupResponseDTO(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupKey()
        );
    }
}