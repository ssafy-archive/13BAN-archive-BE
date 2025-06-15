package com.ssafy.ssafy_13ban_archive.user.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.user.model.request.SignInRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.SignInResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signIn")
    public CommonResponse<SignInResponseDTO> signIn(@RequestBody SignInRequestDTO request) {
        return new CommonResponse<>(userService.signIn(request), HttpStatus.OK);
    }

}
