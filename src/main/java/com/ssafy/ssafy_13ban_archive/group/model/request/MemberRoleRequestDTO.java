package com.ssafy.ssafy_13ban_archive.group.model.request;

import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupRole;
import lombok.Data;

@Data
public class MemberRoleRequestDTO {
    private Integer userId;
    private GroupRole groupRole;
}
