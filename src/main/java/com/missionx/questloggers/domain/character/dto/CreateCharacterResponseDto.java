package com.missionx.questloggers.domain.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CreateCharacterResponseDto {
    private String ocid;
    private String charName;
    private String worldName;
    private String charClass;
    private int charLevel;

    public CreateCharacterResponseDto(String ocid, String charName, String worldName, String charClass, int charLevel) {
        this.ocid = ocid;
        this.charName = charName;
        this.worldName = worldName;
        this.charClass = charClass;
        this.charLevel = charLevel;
    }
}
