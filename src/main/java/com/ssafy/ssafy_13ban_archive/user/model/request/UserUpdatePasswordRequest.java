package com.ssafy.ssafy_13ban_archive.user.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdatePasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

}
