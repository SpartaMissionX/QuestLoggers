package com.missionx.questloggers.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final String message;    // ex) "1번이 회원가입이 완료되었습니다"
    private final T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus code, String message, T data) {
        return ResponseEntity.status(code).body(new ApiResponse<>(message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus code, String message) {
        return ResponseEntity.status(code).body(new ApiResponse<>(message, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus code, String message, T data) {
        return ResponseEntity.status(code).body(new ApiResponse<>(message, data));
    }
}
