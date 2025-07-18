package com.missionx.questloggers.domain.user.repository;

import com.missionx.questloggers.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // email 중복 체크용
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
