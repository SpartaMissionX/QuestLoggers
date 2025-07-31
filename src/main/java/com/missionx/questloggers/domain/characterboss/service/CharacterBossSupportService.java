package com.missionx.questloggers.domain.characterboss.service;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.characterboss.entity.CharacterBoss;
import com.missionx.questloggers.domain.characterboss.exception.AlreadyCharacterBossException;
import com.missionx.questloggers.domain.characterboss.exception.NotFoundCharacterBossExceoption;
import com.missionx.questloggers.domain.characterboss.repository.CharacterBossRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CharacterBossSupportService {

    private final CharacterBossRepository characterBossRepository;

    /**
     * 다른 domain에서 사용
     */
    @Transactional
    public void findById(Long id) {
        characterBossRepository.findById(id).orElseThrow(
                () -> new NotFoundCharacterBossExceoption(HttpStatus.NOT_FOUND,"캐릭터 보스 정보를 찾을 수 없습니다.")
        );
    }

    @Transactional
    public Optional<CharacterBoss> findByCharacterAndBoss(Character character, Boss boss) {
        return characterBossRepository.findByCharacterAndBoss(character, boss);
    }

    @Transactional
    public void save(CharacterBoss characterBoss) {
        characterBossRepository.save(characterBoss);
    }

    @Transactional
    public List<CharacterBoss> findByCharacter(Character character) {
        return characterBossRepository.findByCharacter(character);
    }

    @Transactional
    public List<CharacterBoss> findAll() {
        return characterBossRepository.findAll();
    }
}
