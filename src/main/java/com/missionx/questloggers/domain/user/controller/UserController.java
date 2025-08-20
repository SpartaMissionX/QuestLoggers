package com.missionx.questloggers.domain.user.controller;

import com.missionx.questloggers.domain.auth.service.AuthService;
import com.missionx.questloggers.domain.user.dto.FindUserResponseDto;
import com.missionx.questloggers.domain.user.dto.UpdateApiKeyRequestDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordRequestDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordResponseDto;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 유저 정보 조회
     * 수정 필요
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<FindUserResponseDto>> findUser(@AuthenticationPrincipal LoginUser loginUser) {
        FindUserResponseDto responseDto = userService.findUser(loginUser);
        return ApiResponse.success(HttpStatus.OK, "유저 정보 조회 성공", responseDto);
    }

    /**
     * 비밀번호 변경
     * 수정 필요
     */
    @PatchMapping("/users/password")
    public ResponseEntity<ApiResponse<UpdatePasswordResponseDto>> updatePassword(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestBody @Valid UpdatePasswordRequestDto requestDto) {

        UpdatePasswordResponseDto responseDto = userService.updatePassword(requestDto, loginUser);
        return ApiResponse.success(HttpStatus.OK, "비밀번호 변경 및 토큰 재발급 완료", responseDto);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/users/withdrawal")
    public ResponseEntity<ApiResponse<Void>> withdraw(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        userService.deleteUserById(userId);
        return ApiResponse.success(HttpStatus.OK, "회원 탈퇴가 정상적으로 처리되었습니다.", null);
    }

}
