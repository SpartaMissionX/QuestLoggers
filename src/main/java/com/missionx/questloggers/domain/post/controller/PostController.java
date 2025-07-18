package com.missionx.questloggers.domain.post.controller;

import com.missionx.questloggers.domain.post.dto.CreatePostRequestDto;
import com.missionx.questloggers.domain.post.dto.CreatePostResponseDto;
import com.missionx.questloggers.domain.post.service.PostService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponseDto>> createPost(@RequestBody CreatePostRequestDto createPostRequestDto) {
        CreatePostResponseDto responseDto = postService.createPostService(createPostRequestDto);
        ResponseEntity<ApiResponse<CreatePostResponseDto>> success = ApiResponse.success(HttpStatus.CREATED, "게시글 작성이 완료되었습니다.", responseDto);
        return success;
    }
}
