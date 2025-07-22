package com.missionx.questloggers.domain.user.controller;

import com.missionx.questloggers.domain.user.dto.FindUserResponseDto;
import com.missionx.questloggers.domain.user.dto.UpdateApiKeyRequestDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordRequestDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordResponseDto;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<FindUserResponseDto>> findUser() {
        Long userId = 1L;
        FindUserResponseDto responseDto = userService.findUser(userId);
        return ApiResponse.success(HttpStatus.OK, "유저 정보 조회 성공", responseDto);
    }

    @PatchMapping("/users/password")
    public ResponseEntity<ApiResponse<UpdatePasswordResponseDto>> updatePassword(@RequestBody UpdatePasswordRequestDto requestDto) {
        Long userId = 1L;
        UpdatePasswordResponseDto responseDto = userService.updatePassword(requestDto, userId);
        return ApiResponse.success(HttpStatus.OK, "비밀번호 변경 성공", responseDto);
    }

    @PatchMapping("/users/apikey")
    public void updateApiKey(@RequestBody UpdateApiKeyRequestDto requestDto) {

        ApiResponse.success(HttpStatus.OK, "ApiKey 변경 성공", null);
    }

}
