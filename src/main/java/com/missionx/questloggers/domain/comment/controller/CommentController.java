package com.missionx.questloggers.domain.comment.controller;

import com.missionx.questloggers.domain.comment.dto.CreateCommentRequestDto;
import com.missionx.questloggers.domain.comment.dto.CreateCommentResponseDto;
import com.missionx.questloggers.domain.comment.service.CommentService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CreateCommentResponseDto>> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequestDto requestDto
    ) {
        Long userId = 1L;
        postId = 1L;
        CreateCommentResponseDto responseDto = commentService.createComment(userId, postId, requestDto);
        return ApiResponse.success(HttpStatus.OK, "댓글 작성이 완료되었습니다.", responseDto);
    }

}
