package com.missionx.questloggers.domain.user.service;

import com.missionx.questloggers.domain.user.dto.FindUserResponseDto;
import com.missionx.questloggers.domain.user.dto.UpdatePasswordRequestDto;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.InvalidRequestException;
import com.missionx.questloggers.domain.user.exception.NotFoundUserException;
import com.missionx.questloggers.domain.user.exception.UserException;
import com.missionx.questloggers.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 유저 정보 조회
     */
    public FindUserResponseDto findUser(Long userId) {
        User user = findUserById(userId);
        return new FindUserResponseDto(user.getId(), user.getEmail(), user.getPoint(), user.getRole());
    }

    /**
     * 비밀번호 변경
     */
    public void updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto, Long userId) {
        User user = findUserById(userId);

        if (!passwordEncoder.matches(updatePasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidRequestException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(updatePasswordRequestDto.getNewPassword(), user.getPassword())) {
            throw new UserException(HttpStatus.BAD_REQUEST, "현재 비밀번호와 새 비밀번호가 동일합니다.");
        }

        String newPassword = passwordEncoder.encode(updatePasswordRequestDto.getNewPassword());
        user.updatePassword(newPassword);
    }


    /**
     * 다른 domain에서 사용
     */
    // 회원 탈퇴 - soft delete
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        user.setDeleted(true);
        userRepository.save(user);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundUserException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );
    }

    // 이메일 중복 체크용
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findActiveUserByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new InvalidRequestException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 올바르지 않습니다."));
    }
}
