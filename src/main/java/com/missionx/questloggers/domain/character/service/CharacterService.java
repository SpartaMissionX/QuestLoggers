package com.missionx.questloggers.domain.character.service;

import com.missionx.questloggers.domain.character.dto.GetSerchCharResponseDto;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.post.dto.GetAllPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;

    public List<GetSerchCharResponseDto> serchCharService(String keyword, Pageable pageable) {
        Page<Character> foundCharList = characterRepository.findByCharNameContaining(keyword, pageable);

        return foundCharList.stream()
                .map(character -> {
                    return new GetSerchCharResponseDto(character.getCharName(), character.getCharLevel());
                })
                .collect(Collectors.toList());

    }
}
