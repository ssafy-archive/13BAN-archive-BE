package com.ssafy.ssafy_13ban_archive.user.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private int userId;
    private String loginId;
    private String name;
    private String ssafyNumber;
    private String userRole;

}
