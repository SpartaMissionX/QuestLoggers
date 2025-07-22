package com.missionx.questloggers.domain.characterboss.service;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.service.BossService;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.domain.characterboss.dto.CreateCharBossResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.MyCharInfoResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.UpdateIsClearedResponseDto;
import com.missionx.questloggers.domain.characterboss.entity.CharacterBoss;
import com.missionx.questloggers.domain.characterboss.exception.NotFoundCharacterBossExceoption;
import com.missionx.questloggers.domain.characterboss.repository.CharacterBossRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterBossService {

    private final CharacterBossRepository characterBossRepository;
    private final CharacterService characterService;
    private final BossService bossService;

    @Transactional
    public CreateCharBossResponseDto createCharBoss(Long charId, Long bossId) {
        Character character = characterService.findById(charId);
        Boss boss = bossService.findById(bossId);

        CharacterBoss characterBoss = new CharacterBoss(character, boss);
        characterBossRepository.save(characterBoss);

        return new CreateCharBossResponseDto(characterBoss.getCharacter().getId(), characterBoss.getBoss().getId(), characterBoss.isCleared(), characterBoss.getClearCount());
    }

    @Transactional(readOnly = true)
    public List<MyCharInfoResponseDto> myCharInfo(Long charId) {
        Character character = characterService.findById(charId);
        List<CharacterBoss> characterBossList = characterBossRepository.findByCharacter(character);
        return characterBossList.stream()
                .map(characterBoss -> new MyCharInfoResponseDto(characterBoss.getId(), characterBoss.getCharacter().getId(), characterBoss.getBoss().getId(), characterBoss.isCleared(), characterBoss.getClearCount()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UpdateIsClearedResponseDto updateIsCleared(Long charId, Long bossId) {
        Character character = characterService.findById(charId);
        Boss boss = bossService.findById(bossId);

        CharacterBoss cb = characterBossRepository.findByCharacterAndBoss(character, boss);
        cb.updateIsCleared(true);

        return new UpdateIsClearedResponseDto(cb.getCharacter().getId(), cb.getBoss().getId(), cb.isCleared(), cb.getClearCount());
    }

    /**
     * 다른 domain에서 사용
     */
    public void findById(Long id) {
        characterBossRepository.findById(id).orElseThrow(
                () -> new NotFoundCharacterBossExceoption(HttpStatus.NOT_FOUND,"캐릭터 보스 정보를 찾을 수 없습니다.")
        );
    }

}
