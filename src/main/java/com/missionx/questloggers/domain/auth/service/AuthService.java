package com.missionx.questloggers.domain.auth.service;

import com.missionx.questloggers.domain.auth.dto.SignupRequestDto;
import com.missionx.questloggers.domain.auth.dto.SignupResponseDto;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.UserException;
import com.missionx.questloggers.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    // 회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        if (userService.existsByEmail(signupRequestDto.getEmail())) {
            throw new UserException("이미 존재하는 이메일입니다.");
        }
        User savedUser = userService.createUser(new User(signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getApiKey()));
        return new SignupResponseDto(savedUser.getId(), savedUser.getEmail());
    }
}