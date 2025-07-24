package com.missionx.questloggers.domain.character.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchAllCharResponseDto {
    private final String charName;
    private final int charLevel;
}
