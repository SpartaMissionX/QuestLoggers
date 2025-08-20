package com.missionx.questloggers.global.client;

import com.missionx.questloggers.global.dto.client.*;
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
                        Character character = new Character(
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

    /**
     * 캐릭터 이미지 저장 기능
     */
    @Transactional
    public void updateCharImage(User user, List<Character> characterList) {
        for (Character character : characterList) {
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
                log.warn("데이터를 불러오는데 실패했습니다.");
            }
            character.updateCharImage(characterImage);

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 캐릭터 전투력 저장 기능
     */
    @Transactional
    public void updateCharPower(User user, List<Character> characterList) {
        for (Character character : characterList) {
            String url = "https://open.api.nexon.com/maplestory/v1/character/stat?ocid=" + character.getOcid();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-nxopen-api-key", user.getApiKey());

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String charPower = null;

            try {
                ResponseEntity<CharacterStatDto> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        CharacterStatDto.class
                );
                List<CharacterStatDetailDto> finalStat = response.getBody().getFinalStat();
                for (CharacterStatDetailDto characterStatDetailDto : finalStat) {
                    if (characterStatDetailDto.getStatName().equals("전투력")) {
                        charPower = characterStatDetailDto.getStatValue();
                    }
                }
                character.updateCharPower(charPower);
            } catch (NumberFormatException e) {
                log.warn("데이터를 불러오는데 실패했습니다.");
            } catch (HttpClientErrorException e) {
                log.warn("데이터를 불러오는데 실패했습니다.");
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
