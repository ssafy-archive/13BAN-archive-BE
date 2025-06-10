package com.ssafy.ssafy_13ban_archive.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    private String accessToken;
    private String refreshToken;

}
