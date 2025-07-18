package com.missionx.questloggers.domain.post.controller;

import com.missionx.questloggers.domain.post.dto.CreatePostRequestDto;
import com.missionx.questloggers.domain.post.dto.CreatePostResponseDto;
import com.missionx.questloggers.domain.post.dto.UpdatePostRequestDto;
import com.missionx.questloggers.domain.post.dto.UpdatePostResponseDto;
import com.missionx.questloggers.domain.post.service.PostService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
