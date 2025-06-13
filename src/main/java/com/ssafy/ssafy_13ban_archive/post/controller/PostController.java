package com.ssafy.ssafy_13ban_archive.post.controller;

import com.ssafy.ssafy_13ban_archive.common.model.reponse.CommonResponse;
import com.ssafy.ssafy_13ban_archive.common.model.reponse.SuccessResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostCategory;
import com.ssafy.ssafy_13ban_archive.post.model.entity.PostSubCategory;
import com.ssafy.ssafy_13ban_archive.post.model.request.PostModifyRequestDTO;
import com.ssafy.ssafy_13ban_archive.post.model.request.PostRequestDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.PostListResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.model.response.PostResponseDTO;
import com.ssafy.ssafy_13ban_archive.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 조회", description = "게시글 ID로 특정 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/{postId}")
    public CommonResponse<PostResponseDTO> getPostById(@PathVariable Integer postId) {
        return new CommonResponse<>(postService.getPostById(postId), HttpStatus.OK);
    }


    @Operation(summary = "게시글 목록 조회", description = "카테고리, 서브카테고리, 그룹 ID, 마지막 게시글 ID, 크기를 기준으로 게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    })
    @GetMapping
    public CommonResponse<PostListResponseDTO> getPostsWithLastId(
            @RequestParam(name = "category", defaultValue = "FREE") PostCategory category,
            @RequestParam(name = "subCategory", defaultValue = "NONE") PostSubCategory subCategory,
            @RequestParam("groupId") Integer groupId,
            @RequestParam(name = "lastPostId", defaultValue = "0") Integer lastPostId,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return new CommonResponse<>(
                postService.getPostsWithLastId(category, subCategory, groupId, lastPostId, size),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "게시글 생성",
            description = "새로운 게시글을 생성합니다. 이미지와 파일을 첨부할 수 있습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"post\": { \n" +
                                    "    \"title\": \"게시글 제목\",\n" +
                                    "    \"content\": \"게시글 내용\",\n" +
                                    "    \"groupId\": 1,\n" +
                                    "    \"postCategory\": \"FREE\",\n" +
                                    "    \"postSubCategory\": \"NONE\",\n" +
                                    "    \"imageComments\": {\n" +
                                    "      \"image1.jpg\": \"이미지 설명1\",\n" +
                                    "      \"image2.png\": \"이미지 설명2\"\n" +
                                    "    }\n" +
                                    "  },\n" +
                                    "  \"images\": [\"(binary)\"],\n" +
                                    "  \"files\": [\"(binary)\"]\n" +
                                    "}"
                    )
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<PostResponseDTO> createPost(
            @RequestPart("post") PostRequestDTO postRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return new CommonResponse<>(
                postService.createPost(postRequestDTO, images, files),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "게시글 수정",
            description = "기존 게시글을 수정합니다. 이미지와 파일을 추가하거나 삭제할 수 있습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object"),
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"post\": { \n" +
                                    "    \"title\": \"수정된 게시글 제목\",\n" +
                                    "    \"content\": \"수정된 게시글 내용\",\n" +
                                    "    \"deleteImageIds\": [1, 2],\n" +
                                    "    \"deleteFileIds\": [3, 4],\n" +
                                    "    \"imageComments\": {\n" +
                                    "      \"new_image1.jpg\": \"새 이미지 설명1\"\n" +
                                    "    }\n" +
                                    "  },\n" +
                                    "  \"images\": [\"(binary)\"],\n" +
                                    "  \"files\": [\"(binary)\"]\n" +
                                    "}"
                    )
            )
    )
    @PostMapping(value = "/{postId}/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<PostResponseDTO> modifyPost(
            @PathVariable Integer postId,
            @RequestPart("post") PostModifyRequestDTO postModifyRequestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return new CommonResponse<>(
                postService.modifyPost(postModifyRequestDTO, postId, images, files),
                HttpStatus.OK
        );
    }


    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 관련된 이미지와 파일도 함께 삭제됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @DeleteMapping("/{postId}")
    public CommonResponse<SuccessResponseDTO> deletePost(@PathVariable Integer postId) {
        return new CommonResponse<>(new SuccessResponseDTO(postService.deletePost(postId)), HttpStatus.OK);
    }

}
