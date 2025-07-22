package com.missionx.questloggers.domain.character.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SerchCharResponseDto {
    private final String charName;
    private final int charLevel;
    //private final Long charPower;
    //private final String charImage;
    private final String charServer;
}
