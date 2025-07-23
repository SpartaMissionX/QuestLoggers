package com.missionx.questloggers.domain.character.service;

import com.missionx.questloggers.domain.character.dto.SerchCharResponseDto;
import com.missionx.questloggers.domain.character.dto.*;
import com.missionx.questloggers.domain.character.dto.SerchAllCharResponseDto;
import com.missionx.questloggers.domain.character.dto.AccountListDto;
import com.missionx.questloggers.domain.character.dto.CharacterDto;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.exception.AlreadyHaveOwnerCharacterException;
import com.missionx.questloggers.domain.character.exception.CharacterException;
import com.missionx.questloggers.domain.character.exception.NotFoundCharException;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;

    /**
     * 캐릭터 생성 기능
     */
    @Transactional
    public void createCharList(User user) {
        String url = "https://open.api.nexon.com/maplestory/v1/character/list";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nxopen-api-key", user.getApiKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<AccountListDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                AccountListDto.class
        );

        AccountListDto accountListDto = response.getBody();

        if (accountListDto != null && accountListDto.getAccountList() != null) {
            for (AccountListDto.AccountInfo accountInfo : accountListDto.getAccountList()) {
                if (accountInfo.getCharacterList() != null) {
                    for (CharacterDto characterDto : accountInfo.getCharacterList()) {
                        Character character = new Character(
                                user,
                                characterDto.getOcid(),
                                characterDto.getCharacterName(),
                                characterDto.getWorldName(),
                                characterDto.getCharacterClass(),
                                characterDto.getCharacterLevel()
                        );
                        Character savedCharacter = characterRepository.save(character);
                    }
                }
            }
        }
    }

    /**
     * 캐릭터 조회
     */
    @Transactional
    public List<CharacterListRespnseDto> getCharList(LoginUser loginUser) {
        User user = userService.findUserById(loginUser.getUserId());
        List<Character> characterList = characterRepository.findByUser(user);
        return characterList.stream()
                .map(character -> new CharacterListRespnseDto(character.getId(), character.getOcid(), character.getCharName(), character.getWorldName(), character.getCharClass(), character.getCharLevel()))
                .collect(Collectors.toList());
    }

    /**
     * 유저 리스트 검색
     */
    public PageResponseDto<SerchAllCharResponseDto> serchAllCharService(String keyword, int page , int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Character> charactersPage = characterRepository.findByCharNameContaining(keyword, pageable);

        List<SerchAllCharResponseDto> responseDtos = charactersPage.stream()
                .map(character -> new SerchAllCharResponseDto(character.getCharName(), character.getCharLevel()))
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
     * 유저 단건 검색
     */
    public SerchCharResponseDto serchCharService(Long charId) {
        Character foundChar = characterRepository.findById(charId)
                .orElseThrow(()-> new NotFoundCharException(HttpStatus.NOT_FOUND, "캐릭터 정보를 불러왔습니다."));
        return new SerchCharResponseDto(foundChar.getCharName(), foundChar.getCharLevel(), foundChar.getWorldName());
    }

    /**
     * 대표캐릭터 설정 기능
     * 최초 1번만 사용
     */
    @Transactional
    public SetOwnerCharResponseDto setOwnerChar(LoginUser loginUser, Long charId) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = characterRepository.findById(charId).orElseThrow(
                () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "존재하지 않는 캐릭터입니다."));
        if (character.isOwnerChar() || characterRepository.existsByUserAndOwnerCharTrue(user)) {
            throw new CharacterException(HttpStatus.BAD_REQUEST, "이미 대표캐릭터 설정이 되어있습니다.");
        }

        character.updateOwnerChar(true);

        return new SetOwnerCharResponseDto(character.getCharName(), character.getWorldName(), character.getCharClass(), character.getCharLevel());
    }

    /**
     * 대표 캐릭터 업데이트
     */
    @Transactional
    public UpdateOwnerCharResponseDte updateOwnerChar(LoginUser loginUser ,Long charId) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = findById(charId);

        if (character.isOwnerChar()) {
            throw new AlreadyHaveOwnerCharacterException(HttpStatus.BAD_REQUEST, "이미 대표로 설정된 캐릭터입니다.");
        }
        Character alreadyHaveOwnerChar = characterRepository.findByUserAndOwnerCharTrue(user).orElseThrow(
                () -> new CharacterException(HttpStatus.NOT_FOUND, "대표캐릭터가 존재하지 않습니다.")
        );

        alreadyHaveOwnerChar.updateOwnerChar(false);
        character.updateOwnerChar(true);

        return new UpdateOwnerCharResponseDte(character.getCharName(), character.getWorldName(), character.getCharClass(), character.getCharLevel());
    }

    /**
     * 다른 domain에서 사용
     */
    public Character findById(Long charId) {
        return characterRepository.findById(charId).orElseThrow(
                () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "존재하지 않는 캐릭터입니다.")
        );
    }
}
