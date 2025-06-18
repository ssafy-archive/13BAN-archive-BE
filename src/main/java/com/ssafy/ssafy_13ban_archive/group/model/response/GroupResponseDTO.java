package com.ssafy.ssafy_13ban_archive.group.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDTO {
    private Integer groupId;
    private String groupName;
    private String groupKey;
}
