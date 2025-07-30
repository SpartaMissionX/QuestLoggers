package com.missionx.questloggers.domain.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCharacterResponseDto {
    private String ocid;
    private String charName;
    private String worldName;
    private String charClass;
    private int charLevel;
}
