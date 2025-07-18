package com.missionx.questloggers.domain.auth.controller;

import com.missionx.questloggers.domain.auth.service.AuthService;
import com.missionx.questloggers.domain.user.dto.SignupRequestDto;
import com.missionx.questloggers.domain.user.dto.SignupResponseDto.SignupUserData;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupUserData>> signup(@RequestBody SignupRequestDto signupRequestDto) {
        SignupUserData response = authService.signup(signupRequestDto);
        return ApiResponse.success(HttpStatus.CREATED, "회원가입이 완료되었습니다.", response);
    }

}
