package com.ssafy.ssafy_13ban_archive.user.model.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    private String name;

    @Size(max = 7)
    private String ssafyNumber;

}
