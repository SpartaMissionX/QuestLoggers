package com.missionx.questloggers.domain.user.service;

import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.InvalidRequestUserException;
import com.missionx.questloggers.domain.user.exception.NotFoundUserException;
import com.missionx.questloggers.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSupportService {

    private final UserRepository userRepository;

    /**
     * 다른 domain에서 사용
     */
    @Transactional
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundUserException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );
    }

    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // 이메일 중복 체크용
    @Transactional
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User findActiveUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestUserException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 올바르지 않습니다."));
    }
}
