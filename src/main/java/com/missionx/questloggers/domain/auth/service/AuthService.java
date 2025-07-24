package com.missionx.questloggers.domain.auth.service;

import com.missionx.questloggers.domain.auth.dto.LoginRequestDto;
import com.missionx.questloggers.domain.auth.dto.LoginResponseDto;
import com.missionx.questloggers.domain.auth.dto.SignupRequestDto;
import com.missionx.questloggers.domain.auth.dto.SignupResponseDto;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.enums.Role;
import com.missionx.questloggers.domain.user.exception.DuplicateUserException;
import com.missionx.questloggers.domain.user.exception.InvalidRequestUserException;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.config.AdminEmailProperties;
import com.missionx.questloggers.global.config.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final CharacterService characterService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminEmailProperties adminEmailProperties;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();

        if (userService.existsByEmail(email)) {
            throw new DuplicateUserException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
        }

        // 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        // 관리자 이메일이면 ADMIN, 아니면 USER
        Role role = adminEmailProperties.getEmails().contains(email) ? Role.ADMIN : Role.USER;

        User user = new User(email, encodedPassword, signupRequestDto.getApiKey(), role);
        User savedUser = userService.createUser(user);

        characterService.createCharList(savedUser);

        return new SignupResponseDto(savedUser.getId(), savedUser.getEmail());
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userService.findActiveUserByEmail(loginRequestDto.getEmail());
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidRequestUserException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String jwtToken = jwtTokenProvider.createToken(user);

        return new LoginResponseDto(user.getId(), jwtToken);
    }


    public String updateToken(User user) {
        //JWT 토큰 새로 발급
        return jwtTokenProvider.createToken(user);
    }
}
