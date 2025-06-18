package com.ssafy.ssafy_13ban_archive.group.model.response;

import com.ssafy.ssafy_13ban_archive.group.model.entity.GroupRole;
import com.ssafy.ssafy_13ban_archive.group.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberResponseDTO {
    private Integer userId;
    private String name;
    private String ssafyNumber;
    private GroupRole role;
    private Status status;
}
