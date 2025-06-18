package com.ssafy.ssafy_13ban_archive.group.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.group.model.request.GroupCreateRequestDTO;
import com.ssafy.ssafy_13ban_archive.group.model.response.GroupResponseDTO;
import com.ssafy.ssafy_13ban_archive.group.service.GroupCRUDService;
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
@Tag(name = "그룹 CRUD API", description = "그룹 생성, 수정, 삭제 관련 API 목록")
public class GroupCRUDController {

    private final GroupCRUDService groupCRUDService;

    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "그룹 생성 성공",
                    content = @Content(schema = @Schema(implementation = GroupResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public CommonResponse<GroupResponseDTO> createGroup(
            @Parameter(description = "생성할 그룹 정보", required = true) @RequestBody GroupCreateRequestDTO groupCreateRequestDTO,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo) {
        return new CommonResponse<>(groupCRUDService.createGroup(groupCreateRequestDTO, jwtUserInfo), HttpStatus.CREATED);
    }

    @Operation(summary = "그룹 수정", description = "기존 그룹의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 수정 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없음")
    })
    @PutMapping("/{groupId}")
    public CommonResponse<Boolean> modifyGroup(
            @Parameter(description = "수정할 그룹 정보", required = true) @RequestBody GroupCreateRequestDTO groupCreateRequestDTO,
            @Parameter(description = "수정할 그룹 ID", required = true) @PathVariable Integer groupId,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo) {
        return new CommonResponse<>(groupCRUDService.modifyGroup(groupCreateRequestDTO, groupId, jwtUserInfo), HttpStatus.OK);
    }

    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 삭제 성공",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없음")
    })
    @DeleteMapping("/{groupId}")
    public CommonResponse<Boolean> deleteGroup(
            @Parameter(description = "삭제할 그룹 ID", required = true) @PathVariable Integer groupId,
            @Parameter(description = "인증된 사용자 정보", hidden = true) @AuthenticationPrincipal JwtUserInfo jwtUserInfo) {
        return new CommonResponse<>(groupCRUDService.deleteGroup(groupId, jwtUserInfo), HttpStatus.OK);
    }
}
