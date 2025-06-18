package com.ssafy.ssafy_13ban_archive.group.util;

import com.ssafy.ssafy_13ban_archive.group.exception.GroupNotFoundException;
import com.ssafy.ssafy_13ban_archive.group.exception.UserNotFoundException;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Group;
import com.ssafy.ssafy_13ban_archive.group.model.response.GroupResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.repository.GroupRepository;
import com.ssafy.ssafy_13ban_archive.security.dto.JwtUserInfo;
import com.ssafy.ssafy_13ban_archive.user.model.entity.User;
import com.ssafy.ssafy_13ban_archive.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupUtil {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;


    public User checkUserExists(JwtUserInfo jwtUserInfo) {
        if (jwtUserInfo == null) {
            throw new UserNotFoundException("사용자 정보가 유효하지 않습니다.");
        }
        return userRepository.findById(jwtUserInfo.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public Group checkGroupExists(Integer groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("그룹을 찾을 수 없습니다."));
    }

    public GroupResponseDTO convertToResponseDTO(Group group) {
        return new GroupResponseDTO(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupKey()
        );
    }
}
