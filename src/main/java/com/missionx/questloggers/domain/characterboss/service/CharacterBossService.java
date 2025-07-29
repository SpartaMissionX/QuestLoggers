package com.missionx.questloggers.domain.characterboss.service;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.service.BossService;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.domain.characterboss.dto.CreateCharBossResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.MyCharInfoResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.UpdateIsClearedResponseDto;
import com.missionx.questloggers.domain.characterboss.entity.CharacterBoss;
import com.missionx.questloggers.domain.characterboss.exception.AlreadyCharacterBossException;
import com.missionx.questloggers.domain.characterboss.exception.NotFoundCharacterBossExceoption;
import com.missionx.questloggers.domain.characterboss.repository.CharacterBossRepository;
import com.missionx.questloggers.domain.user.dto.FindUserResponseDto;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.config.security.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterBossService {

    private final CharacterBossRepository characterBossRepository;
    private final UserService userService;
    private final CharacterService characterService;
    private final BossService bossService;

    /**
     * 캐릭터 보스 생성
     */
    @Transactional
    public CreateCharBossResponseDto createCharBoss(LoginUser loginUser, Long bossId) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = characterService.findById(user.getOwnerCharId());
        Boss boss = bossService.findById(bossId);

        if (characterBossRepository.findByCharacterAndBoss(character, boss).isPresent()) {
            throw new AlreadyCharacterBossException(HttpStatus.BAD_REQUEST, "이미 존재하는 캐릭터 보스 입니다.");
        }

        CharacterBoss characterBoss = new CharacterBoss(character, boss);
        characterBossRepository.save(characterBoss);

        return new CreateCharBossResponseDto(characterBoss.getCharacter().getId(), characterBoss.getBoss().getId(), characterBoss.getBoss().getBossName(), characterBoss.isCleared(), characterBoss.getClearCount());
    }

    /**
     * 캐릭터 보스 조회
     */
    @Transactional(readOnly = true)
    public List<MyCharInfoResponseDto> myCharInfo(LoginUser loginUser) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = characterService.findById(user.getOwnerCharId());
        List<CharacterBoss> characterBossList = characterBossRepository.findByCharacter(character);
        return characterBossList.stream()
                .map(characterBoss -> new MyCharInfoResponseDto(characterBoss.getId(), characterBoss.getCharacter().getId(), characterBoss.getBoss().getId(), characterBoss.isCleared(), characterBoss.getClearCount()))
                .collect(Collectors.toList());
    }


    /**
     * 캐릭터의 보스 클리어 여부 수정
     */
    @Transactional
    public UpdateIsClearedResponseDto updateIsCleared(LoginUser loginUser, Long bossId) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = characterService.findById(user.getOwnerCharId());
        Boss boss = bossService.findById(bossId);

        CharacterBoss cb = characterBossRepository.findByCharacterAndBoss(character, boss).orElseThrow(
                () -> new NotFoundCharacterBossExceoption(HttpStatus.NOT_FOUND,"캐릭터 보스 정보를 찾을 수 없습니다.")
        );
        if (cb.isCleared()) {
            throw new AlreadyCharacterBossException(HttpStatus.BAD_REQUEST, "이미 클리어한 보스입니다.");
        } else {
            cb.updateIsCleared(true);
        }

        return new UpdateIsClearedResponseDto(cb.getCharacter().getId(), cb.getBoss().getId(), cb.isCleared(), cb.getClearCount());
    }

    // 스케쥴 ( 월요일 AM 06:00 마다 클리어 여부 초기화 )
    @Scheduled(cron = "0 0 6 * * 1")
    @Transactional
    public void updateIsClearedToFalse() {
        List<CharacterBoss> characterBossList = characterBossRepository.findAll();
        for (CharacterBoss characterBoss : characterBossList) {
            characterBoss.updateIsClearedToFalse();
        }
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
