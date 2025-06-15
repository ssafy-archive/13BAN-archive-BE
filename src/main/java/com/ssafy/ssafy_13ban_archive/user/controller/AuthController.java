package com.ssafy.ssafy_13ban_archive.user.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.SuccessResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.LoginRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.LogoutRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.LoginResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "로그인", description = "로그인 id와 password로 로그인을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "403", description = "로그인 실패")
    })
    @PostMapping("/login")
    public CommonResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return new CommonResponse<>(authService.login(request), HttpStatus.OK);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
    })
    @PostMapping("/logout")
    public CommonResponse<SuccessResponseDTO> logout(@RequestBody LogoutRequestDTO request) {
        return new CommonResponse<>(authService.logout(request), HttpStatus.OK);
    }

}
