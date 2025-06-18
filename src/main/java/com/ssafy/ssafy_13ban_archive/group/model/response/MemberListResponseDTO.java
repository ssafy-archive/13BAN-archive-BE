package com.ssafy.ssafy_13ban_archive.group.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberListResponseDTO {
    private Integer groupId;
    private String groupName;
    private List<GroupMemberResponseDTO> members;
}
