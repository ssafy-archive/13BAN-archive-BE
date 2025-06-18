package com.ssafy.ssafy_13ban_archive.group.model.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleModifyRequestDTO {
    List<MemberRoleRequestDTO> members;
}
