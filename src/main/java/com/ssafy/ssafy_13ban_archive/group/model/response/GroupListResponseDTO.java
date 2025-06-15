package com.ssafy.ssafy_13ban_archive.group.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class GroupListResponseDTO {
    private List<GroupListResponseDTO> groupList;
}
