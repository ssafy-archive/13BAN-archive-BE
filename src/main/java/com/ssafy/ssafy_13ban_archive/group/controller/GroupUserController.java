package com.ssafy.ssafy_13ban_archive.group.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.SuccessResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.model.request.GroupEnterRequestDto;
import com.ssafy.ssafy_13ban_archive.group.model.response.GroupListResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.MemberListResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.service.GroupUserService;
import com.ssafy.ssafy_13ban_archive.security.dto.JwtUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
@PreAuthorize("isAuthenticated()")
@Tag(name = "그룹 사용자 API", description = "그룹 일반 사용자 관련 API 목록")
public class GroupUserController {

    private final GroupUserService groupUserService;

    @Operation(summary = "내 그룹 목록 조회", description = "사용자가 속한 모든 그룹 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = GroupListResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/myGroups")
    public CommonResponse<GroupListResponseDTO> getMyGroups(
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        return new CommonResponse<>(groupUserService.getMyGroups(jwtUserInfo), HttpStatus.OK);
    }

    @Operation(summary = "그룹 탈퇴", description = "사용자가 그룹에서 탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/{groupId}/leave")
    public CommonResponse<SuccessResponseDTO> leaveGroup(
            @Parameter(description = "탈퇴할 그룹 ID", required = true) @PathVariable Integer groupId,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        return new CommonResponse<>(new SuccessResponseDTO(groupUserService.leaveGroup(groupId, jwtUserInfo)), HttpStatus.OK);
    }

    @Operation(summary = "그룹 멤버 목록 조회", description = "그룹의 모든 멤버 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberListResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없음")
    })
    @GetMapping("/{groupId}/members")
    public CommonResponse<MemberListResponseDTO> getGroupMembers(
            @Parameter(description = "조회할 그룹 ID", required = true) @PathVariable Integer groupId,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        return new CommonResponse<>(groupUserService.getGroupMembers(groupId, jwtUserInfo), HttpStatus.OK);
    }

    @Operation(summary = "그룹 가입 요청", description = "사용자가 그룹에 가입 요청을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가입 요청 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 사용자 또는 이미 요청을 보냄")
    })
    @PostMapping("/request")
    public CommonResponse<SuccessResponseDTO> requestJoinGroup(
            @Parameter(description = "그룹 가입 요청 정보", required = true) @RequestBody GroupEnterRequestDto groupEnterRequestDto,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        return new CommonResponse<>(new SuccessResponseDTO(groupUserService.requestJoinGroup(groupEnterRequestDto, jwtUserInfo)), HttpStatus.OK);
    }
}
