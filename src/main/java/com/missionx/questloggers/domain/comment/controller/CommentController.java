package com.missionx.questloggers.domain.comment.controller;

import com.missionx.questloggers.domain.comment.dto.*;
import com.missionx.questloggers.domain.comment.service.CommentService;
import com.missionx.questloggers.global.dto.ApiResponse;
import com.missionx.questloggers.global.dto.PageResponseDto;
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
        CreateCommentResponseDto responseDto = commentService.createComment(userId, postId, requestDto);
        return ApiResponse.success(HttpStatus.CREATED, "댓글 작성이 완료되었습니다.", responseDto);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<PageResponseDto<FindAllCommentResponseDto>>> findAllComment(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponseDto<FindAllCommentResponseDto> comments = commentService.findAllComment(postId, page, size);
        return ApiResponse.success(HttpStatus.OK, "댓글 조회가 완료되었습니다.", comments);
    }


    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<UpdateCommentResponseDto>> updateComment(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequestDto requestDto
    ) {
        UpdateCommentResponseDto responseDto = commentService.updateComment(commentId, requestDto);
        return ApiResponse.success(HttpStatus.OK, "댓글 수정이 완료되었습니다.", responseDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.success(HttpStatus.OK, "댓글 삭제가 완료되었습니다.", null);
    }
}
