package com.missionx.questloggers.domain.character.service;

import com.missionx.questloggers.domain.character.dto.SearchCharResponseDto;
import com.missionx.questloggers.domain.character.dto.*;
import com.missionx.questloggers.domain.character.dto.SearchAllCharResponseDto;
import com.missionx.questloggers.domain.character.dto.AccountListDto;
import com.missionx.questloggers.domain.character.dto.CharacterDto;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.exception.AlreadyHaveOwnerCharacterException;
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
     * 본인 캐릭터 전체 조회
     */
    @Transactional(readOnly = true)
    public List<CharacterListRespnseDto> getCharList(LoginUser loginUser) {
        User user = userService.findUserById(loginUser.getUserId());
        List<Character> characterList = characterRepository.findByUser(user);
        return characterList.stream()
                .map(character -> new CharacterListRespnseDto(character.getId(), character.getOcid(), character.getCharName(), character.getWorldName(), character.getCharClass(), character.getCharLevel(), character.isOwnerChar()))
                .collect(Collectors.toList());
    }

    /**
     * 본인 대표 캐릭터 조회
     */
    @Transactional
    public GetOwnerCharResponseDto getOwnerChar(LoginUser loginUser) {
        User user = userService.findUserById(loginUser.getUserId());
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
     * 대표캐릭터 설정 기능
     */
    @Transactional
    public SetOwnerCharResponseDto setOwnerChar(LoginUser loginUser, Long charId) {
        User user = userService.findUserById(loginUser.getUserId());
        List<Character> byUser = characterRepository.findByUser(user);

        for (Character c : byUser) {
            if (c.getId().equals(charId) && c.isOwnerChar()) {
                throw new AlreadyHaveOwnerCharacterException(HttpStatus.BAD_REQUEST, "이미 대표 캐릭터로 설정되어 있습니다.");
            } else if (c.getId().equals(charId)) {
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
        throw new NotFoundCharException(HttpStatus.NOT_FOUND, "캐릭터를 찾을 수 없습니다.");
    }



    /**
     * 다른 domain에서 사용
     */

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

    public Character findById(Long charId) {
        return characterRepository.findById(charId).orElseThrow(
                () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "존재하지 않는 캐릭터입니다.")
        );
    }
}
