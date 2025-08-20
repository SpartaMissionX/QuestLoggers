package com.missionx.questloggers.domain.character.repository;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Page<Character> findByCharNameContaining(String charName, Pageable pageable);
    Optional<Character> findByUserAndOwnerCharTrue(User user);
    boolean existsByUserAndOwnerCharTrue(User user);
    List<Character> findByUser(User user);
    boolean existsByOcid(String ocid);

}
