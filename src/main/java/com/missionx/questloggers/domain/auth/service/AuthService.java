package com.missionx.questloggers.domain.auth.service;

import com.missionx.questloggers.domain.user.dto.SignupRequestDto;
import com.missionx.questloggers.domain.user.dto.SignupResponseDto;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.UserException;
import com.missionx.questloggers.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import com.missionx.questloggers.domain.user.dto.SignupResponseDto.SignupUserData;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    // 회원가입
    public SignupUserData signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String apiKey = signupRequestDto.getApiKey();

        // 이메일 중복 검사
        if (userService.existsByEmail(email)) {
            throw new UserException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 유효성 검사
        if (!isValidPassword(password)) {
            throw new UserException("비밀번호는 최소 8자, 대소문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        // API 키 유효성 검사
        if (!isValidApiKey(apiKey)) {
            throw new UserException("존재하지 않거나 잘못된 API키입니다.");
        }

        // 유저 저장
        User savedUser = userService.createUser(new User(email, password, apiKey));

        // SignupUserData만 리턴
        return new SignupUserData(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(String password) {
        return password != null &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*]).{8,}$");
    }

    // API 키 유효성 검사 메서드
    private boolean isValidApiKey(String apiKey) {
        return apiKey != null && apiKey.length() >= 10;
    }

    public void login() {

    }
}
