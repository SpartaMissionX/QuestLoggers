package com.missionx.questloggers.global.exception;

import com.missionx.questloggers.domain.comment.exception.UnauthorizedCommentAccessException;
import com.missionx.questloggers.domain.post.exception.UnauthorizedPostAccessException;
import com.missionx.questloggers.domain.user.exception.DuplicateUserException;
import com.missionx.questloggers.domain.user.exception.InvalidPasswordException;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이메일 중복
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateUserException(DuplicateUserException duplicateUserException) {
        log.warn("DuplicateUserException: {}", duplicateUserException.getMessage());
        return ApiResponse.error(HttpStatus.CONFLICT, duplicateUserException.getMessage());
    }

    // 비밀번호 틀렸을때
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidPasswordException(InvalidPasswordException invalidPasswordException) {
        log.warn("InvalidPasswordException: {}", invalidPasswordException.getMessage());
        return ApiResponse.error(HttpStatus.BAD_REQUEST, invalidPasswordException.getMessage());
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

}
