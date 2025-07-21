package com.missionx.questloggers.domain.post.controller;

import com.missionx.questloggers.domain.post.dto.*;
import com.missionx.questloggers.domain.post.service.PostService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponseDto>> createPost(@RequestBody CreatePostRequestDto createPostRequestDto) {
        CreatePostResponseDto responseDto = postService.createPostService(createPostRequestDto);
        return ApiResponse.success(HttpStatus.CREATED, "게시글 작성이 완료되었습니다.", responseDto);
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<UpdatePostResponseDto>> updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody UpdatePostRequestDto updatePostRequestDto
            ) {
        UpdatePostResponseDto responseDto = postService.updatePostService(postId, updatePostRequestDto);
        return ApiResponse.success(HttpStatus.OK, "게시글 수정이 완료되었습니다.", responseDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<GetAllPostResponseDto>>> getAllPost(
            @RequestParam("keyword") String keyword,
            @PageableDefault(size = 10,page = 0,sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<GetAllPostResponseDto> allPostService = postService.getAllPostService(keyword, pageable);
        return ApiResponse.success(HttpStatus.ACCEPTED,"게시글 전체 조회 성공.", allPostService);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<GetPostResponseDto>> getPost (@PathVariable("postId") Long postId) {
        GetPostResponseDto getPostResponseDto = postService.getPostService(postId);
        return ApiResponse.success(HttpStatus.FOUND,"게시글 조회 성공", getPostResponseDto);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Object>> deletePost (@PathVariable("postId") Long postId) {
        postService.deletePostService(postId);
        return ApiResponse.success(HttpStatus.OK,"게시글 삭제가 완료되었습니다.", null);
    }

}
