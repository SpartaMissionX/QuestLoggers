package com.missionx.questloggers.domain.character.service;

import com.missionx.questloggers.domain.character.dto.GetSerchCharResponseDto;
import com.missionx.questloggers.domain.character.dto.AccountListDto;
import com.missionx.questloggers.domain.character.dto.CharacterDto;
import com.missionx.questloggers.domain.character.dto.CreateCharacterResponseDto;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.exception.CharacterException;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.post.dto.GetAllPostResponseDto;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public List<GetSerchCharResponseDto> serchCharService(String keyword, Pageable pageable) {
        Page<Character> foundCharList = characterRepository.findByCharNameContaining(keyword, pageable);

        return foundCharList.stream()
                .map(character -> {
                    return new GetSerchCharResponseDto(character.getCharName(), character.getCharLevel());
                })
                .collect(Collectors.toList());

    }
}
