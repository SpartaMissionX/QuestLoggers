package com.missionx.questloggers.domain.character.service;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.exception.NotFoundCharException;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CharacterSupportService {

    private final CharacterRepository characterRepository;

    @Transactional
    public Character findById(Long charId) {
        return characterRepository.findById(charId).orElseThrow(
                () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "존재하지 않는 캐릭터입니다.")
        );
    }

    @Transactional
    public Character findByMainCharId(Long mainCharId) {
        if (mainCharId == null) {
            throw new NotFoundCharException(HttpStatus.NOT_FOUND, "대표 캐릭터를 설정해주세요");
        }
        return characterRepository.findById(mainCharId).orElseThrow(
                () -> new NotFoundCharException(HttpStatus.NOT_FOUND, "대표 캐릭터를 찾을 수 없습니다.")
        );
    }
}
