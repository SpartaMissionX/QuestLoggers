package com.missionx.questloggers.domain.character.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetSerchCharResponseDto {
    private final String charName;
    private final int charLevel;
}
