package com.ssafy.ssafy_13ban_archive.user.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.SuccessResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.SignInRequestDTO;
import com.ssafy.ssafy_13ban_archive.user.model.request.UserUpdatePasswordRequest;
import com.ssafy.ssafy_13ban_archive.user.model.request.UserUpdateRequest;
import com.ssafy.ssafy_13ban_archive.user.model.response.SignInResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.model.response.UserResponseDTO;
import com.ssafy.ssafy_13ban_archive.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "사용자 회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "회원가입 실패")
    })
    @PostMapping
    public CommonResponse<SignInResponseDTO> createUser(@RequestBody SignInRequestDTO request) {
        return new CommonResponse<>(userService.createUser(request), HttpStatus.OK);
    }

    @Operation(summary = "특정 사용자 조회", description = "userId를 가지는 사용자를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @GetMapping("/{userId}")
    public CommonResponse<UserResponseDTO> getUser(@PathVariable Integer userId) {
        return new CommonResponse<>(userService.getUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "특정 사용자 업데이트", description = "userId를 가지는 사용자 정보를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @PutMapping("/{userId}")
    public CommonResponse<SuccessResponseDTO> updateUser(@PathVariable Integer userId, @RequestBody UserUpdateRequest request) {
        return new CommonResponse<>(userService.updateUser(userId, request), HttpStatus.OK);
    }

    @Operation(summary = "특정 사용자 비밀번호 업데이트", description = "userId를 가지는 사용자 비밀번호를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 비밀번호 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    @PutMapping("/{userId}/password")
    public CommonResponse<SuccessResponseDTO> updateUserPassword(@PathVariable Integer userId, @RequestBody UserUpdatePasswordRequest request) {
        return new CommonResponse<>(userService.updateUserPassword(userId, request), HttpStatus.OK);
    }

}
