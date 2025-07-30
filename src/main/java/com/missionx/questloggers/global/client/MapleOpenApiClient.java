package com.missionx.questloggers.global.client;

import com.missionx.questloggers.domain.character.exception.CharacterException;
import com.missionx.questloggers.global.dto.client.AccountListDto;
import com.missionx.questloggers.global.dto.client.CharacterBasicDto;
import com.missionx.questloggers.global.dto.client.CharacterDto;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapleOpenApiClient {

    private final RestTemplate restTemplate;
    private final CharacterRepository characterRepository;

    /**
     * 캐릭터 생성 기능
     */
    @Transactional
    public List<Character> createCharList(User user) {
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
        List<Character> characterList = new ArrayList<>();

        if (accountListDto != null && accountListDto.getAccountList() != null) {
            for (AccountListDto.AccountInfo accountInfo : accountListDto.getAccountList()) {
                if (accountInfo.getCharacterList() != null) {
                    for (CharacterDto characterDto : accountInfo.getCharacterList()) {
                        com.missionx.questloggers.domain.character.entity.Character character = new com.missionx.questloggers.domain.character.entity.Character(
                                user,
                                characterDto.getOcid(),
                                characterDto.getCharacterName(),
                                characterDto.getWorldName(),
                                characterDto.getCharacterClass(),
                                characterDto.getCharacterLevel()
                        );
                        Character savedCharacter = characterRepository.save(character);
                        characterList.add(savedCharacter);
                    }
                }
            }
        }

        return characterList;

    }

    @Transactional
    public void updateCharImage(User user, List<Character> charList) {
        for (Character character : charList) {
            String url = "https://open.api.nexon.com/maplestory/v1/character/basic?ocid=" + character.getOcid();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-nxopen-api-key", user.getApiKey());

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String characterImage = null;

            try {
                ResponseEntity<CharacterBasicDto> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        CharacterBasicDto.class
                );
                characterImage = response.getBody().getCharacterImage();
            } catch (HttpClientErrorException e) {
                log.warn("데이터를 불러오는데 실패했습니다. warn : {}" , e);
            }

            character.updateCharImage(characterImage);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
