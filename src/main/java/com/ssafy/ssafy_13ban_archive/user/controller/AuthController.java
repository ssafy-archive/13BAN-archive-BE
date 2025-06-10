package com.ssafy.ssafy_13ban_archive.user.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.user.model.request.LoginRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.LogoutRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.ActionResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.LoginResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CommonResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return new CommonResponse<>(authService.login(request), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public CommonResponse<ActionResponseDTO> logout(@RequestBody LogoutRequestDTO request) {
        return new CommonResponse<>(authService.logout(request), HttpStatus.OK);
    }

}
