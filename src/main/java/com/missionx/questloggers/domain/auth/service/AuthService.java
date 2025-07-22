package com.missionx.questloggers.domain.auth.service;

import com.missionx.questloggers.domain.auth.dto.LoginRequestDto;
import com.missionx.questloggers.domain.auth.dto.LoginResponseDto;
import com.missionx.questloggers.domain.auth.dto.SignupRequestDto;
import com.missionx.questloggers.domain.auth.dto.SignupResponseDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordRequestDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordResponseDto;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.UserException;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.config.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        if (userService.existsByEmail(signupRequestDto.getEmail())) {
            throw new UserException("이미 존재하는 이메일입니다.");
        }

        // 암호화(해싱)
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User savedUser = userService.createUser(new User(signupRequestDto.getEmail(), encodedPassword, signupRequestDto.getApiKey()));

        return new SignupResponseDto(savedUser.getId(), savedUser.getEmail());
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userService.findActiveUserByEmail(loginRequestDto.getEmail());
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }

        String jwtToken = jwtTokenProvider.createToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name(), // enum을 string으로 바꿔야함
                user.getApiKey(),
                user.getPoint()
        );

        return new LoginResponseDto(user.getId(), jwtToken);
    }

    public UpdatePasswordResponseDto updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto, Long userId) {
        userService.updatePassword(updatePasswordRequestDto, userId);
        //유저 재조회
        User updatedUser = userService.findUserById(userId);
        //JWT 토큰 새로 발급
        String newToken = jwtTokenProvider.createToken(
                updatedUser.getId(),
                updatedUser.getEmail(),
                updatedUser.getRole().name(),
                updatedUser.getApiKey(),
                updatedUser.getPoint()
        );

        return new UpdatePasswordResponseDto(
                updatedUser.getId(),
                updatedUser.getEmail(),
                updatedUser.getPoint(),
                updatedUser.getRole(),
                newToken
        );

    }
}
