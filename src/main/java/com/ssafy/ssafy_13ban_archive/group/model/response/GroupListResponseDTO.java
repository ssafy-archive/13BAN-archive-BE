package com.ssafy.ssafy_13ban_archive.group.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupListResponseDTO {
    private List<GroupRoleResponseDTO> groupList;
}
