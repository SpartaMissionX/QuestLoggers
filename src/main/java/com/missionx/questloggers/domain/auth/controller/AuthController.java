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

}
