package com.missionx.questloggers.domain.character.service;

import com.missionx.questloggers.domain.character.dto.AccountListDto;
import com.missionx.questloggers.domain.character.dto.CharacterDto;
import com.missionx.questloggers.domain.character.dto.CreateCharacterResponseDto;
import com.missionx.questloggers.domain.character.dto.SetOwnerCharResponseDto;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.exception.CharacterException;
import com.missionx.questloggers.domain.character.exception.NotFoundCharException;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserService userService;
    private final RestTemplate restTemplate;

    @Transactional
    public AccountListDto getCharList(Long userId) {
        User user = userService.findUserById(userId);
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

        return response.getBody();
    }


    /**
     * 캐릭터 생성 기능
     */
    @Transactional
    public void createCharList(Long userId) {
        User user = userService.findUserById(userId);
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
                        characterRepository.save(character);
                    }
                }
            }
        }
    }

    /**
     * 대표캐릭터 설정 기능
     * 최초 1번만 사용
     */
    public SetOwnerCharResponseDto setOwnerChar(Long charId) {
        Character character = characterRepository.findById(charId).orElseThrow(
                () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "존재하지 않는 캐릭터입니다."));

        character.updateOwnerChar(true);

        return new SetOwnerCharResponseDto(character.getCharName(), character.getWorldName(), character.getCharClass(), character.getCharLevel());
    }
}
