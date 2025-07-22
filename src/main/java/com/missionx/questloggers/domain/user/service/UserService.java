package com.missionx.questloggers.domain.user.service;

import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.UserNotFoundException;
import com.missionx.questloggers.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("이거")
        );
    }

    // 회원 탈퇴 - soft delete
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));

        user.setDeleted(true);
        userRepository.save(user);
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
                .orElseThrow(() -> new UserNotFoundException("이메일 또는 비밀번호가 올바르지 않습니다."));
    }
}
