package com.missionx.questloggers.global.exception;

import com.missionx.questloggers.domain.boss.exception.BossException;
import com.missionx.questloggers.domain.character.exception.CharacterException;
import com.missionx.questloggers.domain.characterboss.exception.CharacterBossException;
import com.missionx.questloggers.domain.comment.exception.CommentException;
import com.missionx.questloggers.domain.comment.exception.UnauthorizedCommentAccessException;
import com.missionx.questloggers.domain.post.exception.PostException;
import com.missionx.questloggers.domain.post.exception.UnauthorizedPostAccessException;
import com.missionx.questloggers.domain.user.exception.UserException;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // User
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(UserException e) {
        log.warn("{}: {}", e.getClass(), e.getMessage());
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    // Post
    @ExceptionHandler(PostException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostException(PostException e) {
        log.warn("{}: {}", e.getClass(), e.getMessage());
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    // Comment
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostException(CommentException e) {
        log.warn("{}: {}", e.getClass(), e.getMessage());
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    // Character
    @ExceptionHandler(CharacterException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostException(CharacterException e) {
        log.warn("{}: {}", e.getClass(), e.getMessage());
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    // Boss
    @ExceptionHandler(BossException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostException(BossException e) {
        log.warn("{}: {}", e.getClass(), e.getMessage());
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    // CharacterBoss
    @ExceptionHandler(CharacterBossException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostException(CharacterBossException e) {
        log.warn("{}: {}", e.getClass(), e.getMessage());
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }



    // post 수정,삭제 권한 없을 때(작성자가 아닐 때)
    @ExceptionHandler(UnauthorizedPostAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedPostAccess(UnauthorizedPostAccessException ex) {
        return ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // comment 수정, 삭제 권한 없을 때(댓글 작성자가 아닐 때)
    @ExceptionHandler(UnauthorizedCommentAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedCommentAccess(UnauthorizedCommentAccessException ex) {
        return ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // vaildation 오류
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (exsiting, replacement) -> exsiting
                ));

        return ApiResponse.error(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다", errors);
    }

}
