package com.missionx.questloggers.domain.user.controller;

import com.missionx.questloggers.domain.auth.service.AuthService;
import com.missionx.questloggers.domain.user.dto.FindUserResponseDto;
import com.missionx.questloggers.domain.user.dto.UpdateApiKeyRequestDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordRequestDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordResponseDto;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    /**
     * 유저 정보 조회
     * 수정 필요
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<FindUserResponseDto>> findUser() {
        Long userId = 1L;
        FindUserResponseDto responseDto = userService.findUser(userId);
        return ApiResponse.success(HttpStatus.OK, "유저 정보 조회 성공", responseDto);
    }

    /**
     * 비밀번호 변경
     * 수정 필요
     */
    @PostMapping("/users/password/{userId}")
    public ResponseEntity<ApiResponse<UpdatePasswordResponseDto>> updatePassword(
            @PathVariable Long userId,
            @RequestBody @Valid UpdatePasswordRequestDto requestDto) {

        UpdatePasswordResponseDto response = authService.updatePassword(requestDto, userId);
        return ApiResponse.success(HttpStatus.OK, "비밀번호 변경 및 토큰 재발급 완료", response);
    }

}
