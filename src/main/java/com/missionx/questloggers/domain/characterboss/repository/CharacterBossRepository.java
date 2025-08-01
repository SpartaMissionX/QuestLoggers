package com.missionx.questloggers.domain.characterboss.repository;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.characterboss.entity.CharacterBoss;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterBossRepository extends JpaRepository<CharacterBoss, Long> {
    List<CharacterBoss> findByCharacter(Character character);
    Optional<CharacterBoss> findByCharacterAndBoss(Character character, Boss boss);
    boolean existsByCharacterAndBoss(Character character, Boss boss);

}
