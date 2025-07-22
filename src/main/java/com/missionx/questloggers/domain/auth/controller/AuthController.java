package com.missionx.questloggers.domain.auth.controller;

import com.missionx.questloggers.domain.auth.dto.LoginRequestDto;
import com.missionx.questloggers.domain.auth.dto.LoginResponseDto;
import com.missionx.questloggers.domain.auth.dto.SignupResponseDto;
import com.missionx.questloggers.domain.auth.service.AuthService;
import com.missionx.questloggers.domain.auth.dto.SignupRequestDto;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        SignupResponseDto signupResponseDto = authService.signup(signupRequestDto);
        return ApiResponse.success(HttpStatus.CREATED, "회원가입이 완료되었습니다.", signupResponseDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return ApiResponse.success(HttpStatus.OK, "로그인에 성공했습니다.", loginResponseDto);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        return ApiResponse.success(HttpStatus.OK, "로그아웃이 완료되었습니다.", null);
    }

    @DeleteMapping("/auth/withdrawal")
    public ResponseEntity<ApiResponse<Void>> withdraw(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        userService.deleteUserById(userId);
        return ApiResponse.success(HttpStatus.OK, "회원 탈퇴가 정상적으로 처리되었습니다.", null);
    }
}
