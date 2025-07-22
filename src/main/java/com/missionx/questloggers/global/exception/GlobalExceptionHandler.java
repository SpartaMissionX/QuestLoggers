package com.missionx.questloggers.global.exception;

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

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateUserException(DuplicateUserException duplicateUserException) {
        log.warn("DuplicateUserException: {}", duplicateUserException.getMessage());
        return ApiResponse.error(HttpStatus.CONFLICT, duplicateUserException.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidPasswordException(InvalidPasswordException invalidPasswordException) {
        log.warn("InvalidPasswordException: {}", invalidPasswordException.getMessage());
        return ApiResponse.error(HttpStatus.BAD_REQUEST, invalidPasswordException.getMessage());
    }
}
