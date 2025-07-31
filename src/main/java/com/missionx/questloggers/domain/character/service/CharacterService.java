package com.missionx.questloggers.domain.character.service;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.service.BossSupportService;
import com.missionx.questloggers.domain.character.dto.SearchCharResponseDto;
import com.missionx.questloggers.domain.character.dto.*;
import com.missionx.questloggers.domain.character.dto.SearchAllCharResponseDto;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.exception.AlreadyHaveOwnerCharacterException;
import com.missionx.questloggers.domain.character.exception.NotFoundCharException;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.characterboss.dto.CreateCharBossResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.MyCharInfoResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.UpdateIsClearedResponseDto;
import com.missionx.questloggers.domain.characterboss.entity.CharacterBoss;
import com.missionx.questloggers.domain.characterboss.exception.AlreadyCharacterBossException;
import com.missionx.questloggers.domain.characterboss.service.CharacterBossSupportService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserSupportService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserSupportService userSupportService;
    private final BossSupportService bossSupportService;
    private final CharacterBossSupportService characterBossSupportService;
    private final CharacterSupportService characterSupportService;

    /**
     * 유저 캐릭터 리스트 검색
     */
    public PageResponseDto<SearchAllCharResponseDto> serchAllCharService(String keyword, int page , int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Character> charactersPage = characterRepository.findByCharNameContaining(keyword, pageable);

        List<SearchAllCharResponseDto> responseDtos = charactersPage.stream()
                .map(character -> new SearchAllCharResponseDto(character.getId(), character.getCharName(), character.getWorldName(), character.getCharClass(), character.getCharLevel()))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                responseDtos,
                charactersPage.getNumber() + 1,
                charactersPage.getSize(),
                charactersPage.getTotalElements(),
                charactersPage.getTotalPages(),
                charactersPage.isLast()
        );
    }

    /**
     * 유저 캐릭터 단건 검색
     */
    public SearchCharResponseDto serchCharService(Long charId) {
        Character foundChar = characterRepository.findById(charId)
                .orElseThrow(()-> new NotFoundCharException(HttpStatus.NOT_FOUND, "존재하지 않는 캐릭터입니다."));
        return new SearchCharResponseDto(foundChar.getId(), foundChar.getCharName(), foundChar.getWorldName(), foundChar.getCharClass(), foundChar.getCharLevel());
    }

    /**
     * 본인 캐릭터 전체 조회
     */
    @Transactional(readOnly = true)
    public List<CharacterListRespnseDto> getCharList(LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        List<Character> characterList = characterRepository.findByUser(user);
        return characterList.stream()
                .map(character -> new CharacterListRespnseDto(character.getId(), character.getOcid(), character.getCharName(), character.getWorldName(), character.getCharClass(), character.getCharLevel(), character.isOwnerChar()))
                .collect(Collectors.toList());
    }

    /**
     * 대표캐릭터 설정 기능
     */
    @Transactional
    public SetOwnerCharResponseDto setOwnerChar(LoginUser loginUser, SetOwnerCharRequestDto requestDto) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        List<Character> byUser = characterRepository.findByUser(user);

        for (Character c : byUser) {
            if (c.getId().equals(requestDto.getCharId()) && c.isOwnerChar()) {
                throw new AlreadyHaveOwnerCharacterException(HttpStatus.BAD_REQUEST, "이미 대표 캐릭터로 설정되어 있습니다.");
            } else if (c.getId().equals(requestDto.getCharId())) {
                for (Character c1 : byUser) {
                    if (c1.isOwnerChar()) {
                        c1.updateOwnerChar(false);
                    }
                }
                c.updateOwnerChar(true);
                user.updateOwnerChar(c.getId(), c.getCharName());
                return new SetOwnerCharResponseDto(c.getCharName(), c.getWorldName(), c.getCharClass(), c.getCharLevel());
            }
        }
        throw new NotFoundCharException(HttpStatus.NOT_FOUND, "본인 캐릭터만 대표캐릭터로 설정할 수 있습니다.");
    }

    /**
     * 본인 대표 캐릭터 조회
     */
    @Transactional
    public GetOwnerCharResponseDto getOwnerChar(LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        if (user.getOwnerCharId() == null) {
            throw new NotFoundCharException(HttpStatus.NOT_FOUND, "대표 캐릭터를 설정해 주세요.");
        } else {
            Character character = characterRepository.findById(user.getOwnerCharId()).orElseThrow(
                    () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "대표 캐릭터를 찾을 수 없습니다. 다시 설정해주세요")
            );
            return new GetOwnerCharResponseDto(character);
        }
    }

    /**
     * 대표 캐릭터의 보스 생성
     */
    @Transactional
    public CreateCharBossResponseDto createCharBoss(CreateCharacterBossRequestDto requestDto, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character character = characterRepository.findById(user.getOwnerCharId()).orElseThrow(
                () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "대표 캐릭터를 찾을 수 없습니다.")
        );
        Boss boss = bossSupportService.findById(requestDto.getBossId());

        CharacterBoss characterBoss = characterBossSupportService.findByCharacterAndBoss(character, boss);

        characterBossSupportService.save(characterBoss);

        return new CreateCharBossResponseDto(characterBoss.getCharacter().getId(), characterBoss.getBoss().getId(), characterBoss.getBoss().getBossName(), characterBoss.isCleared(), characterBoss.getClearCount());
    }

    /**
     * 대표 캐릭터의 보스 조회
     */
    @Transactional(readOnly = true)
    public List<MyCharInfoResponseDto> myCharInfo(LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character character = characterSupportService.findByMainCharId(user.getOwnerCharId());
        List<CharacterBoss> characterBossList = characterBossSupportService.findByCharacter(character);
        return characterBossList.stream()
                .map(characterBoss -> new MyCharInfoResponseDto(characterBoss.getId(), characterBoss.getCharacter().getId(), characterBoss.getBoss().getId(), characterBoss.isCleared(), characterBoss.getClearCount()))
                .collect(Collectors.toList());
    }

    /**
     * 대표 캐릭터의 보스 클리어 여부 수정
     */
    @Transactional
    public UpdateIsClearedResponseDto updateIsCleared(LoginUser loginUser, Long bossId) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character character = characterSupportService.findByMainCharId(user.getOwnerCharId());
        Boss boss = bossSupportService.findById(bossId);

        CharacterBoss characterBoss = characterBossSupportService.findByCharacterAndBoss(character, boss);
        if (characterBoss.isCleared()) {
            throw new AlreadyCharacterBossException(HttpStatus.BAD_REQUEST, "이미 클리어한 보스입니다.");
        } else {
            characterBoss.updateIsCleared(true);
        }

        return new UpdateIsClearedResponseDto(characterBoss.getCharacter().getId(), characterBoss.getBoss().getId(), characterBoss.isCleared(), characterBoss.getClearCount());
    }


    // 스케쥴 ( 월요일 AM 06:00 마다 클리어 여부 초기화 )
    @Scheduled(cron = "0 0 6 * * 1")
    @Transactional
    public void updateIsClearedToFalse() {
        List<CharacterBoss> characterBossList = characterBossSupportService.findAll();
        for (CharacterBoss characterBoss : characterBossList) {
            characterBoss.updateIsClearedToFalse();
        }
    }


}
