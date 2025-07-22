package com.missionx.questloggers.domain.character.repository;

import com.missionx.questloggers.domain.character.entity.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Page<Character> findByCharNameContaining(String charName, Pageable pageable);
}
