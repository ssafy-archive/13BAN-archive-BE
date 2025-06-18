package com.ssafy.ssafy_13ban_archive.group.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.group.model.request.MemberAcceptRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.request.RoleModifyRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.MemberListResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.service.GroupAdminService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
@Tag(name = "그룹 관리자 API", description = "그룹 관리자 권한 관련 API 목록")
public class GroupAdminController {

    private final GroupAdminService groupAdminService;

    @Operation(summary = "멤버 역할 수정", description = "그룹 내 멤버의 역할을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "역할 수정 성공",
                    content = @Content(schema = @Schema(implementation = MemberListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "그룹 또는 멤버 찾을 수 없음")
    })
    @PutMapping("/{groupId}/members")
    public CommonResponse<MemberListResponseDTO> modifyMemberRole(
            @Parameter(description = "역할 수정 정보", required = true) @RequestBody RoleModifyRequestDTO roleModifyRequestDTO,
            @Parameter(description = "그룹 ID", required = true) @PathVariable Integer groupId,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        return new CommonResponse<>(groupAdminService.modifyMemberRole(roleModifyRequestDTO, groupId, jwtUserInfo), HttpStatus.OK);
    }

    @Operation(summary = "멤버 가입 요청 수락", description = "그룹 가입 요청을 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가입 요청 수락 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "그룹 또는 멤버 찾을 수 없음")
    })
    @PostMapping("/{groupId}/members/accept")
    public CommonResponse<Boolean> acceptMemberRequest(
            @Parameter(description = "멤버 수락 정보", required = true) @RequestBody MemberAcceptRequestDTO memberAcceptRequestDTO,
            @Parameter(description = "그룹 ID", required = true) @PathVariable Integer groupId,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo
    ) {
        return new CommonResponse<>(groupAdminService.acceptMemberRequest(memberAcceptRequestDTO, groupId, jwtUserInfo), HttpStatus.OK);
    }
}
