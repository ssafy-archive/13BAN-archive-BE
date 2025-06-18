package com.ssafy.ssafy_13ban_archive.group.model.response;

import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRoleResponseDTO {
    private Integer groupId;
    private String groupName;
    private String groupKey;
    private GroupRole groupRole;
}
