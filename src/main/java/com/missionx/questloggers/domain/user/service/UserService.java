package com.missionx.questloggers.domain.user.service;

import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("이거")
        );
    }

    // 이메일 중복 체크용
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
