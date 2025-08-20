package com.missionx.questloggers.domain.character.dto;

import com.missionx.questloggers.domain.character.entity.Character;
import lombok.Getter;

@Getter
public class GetOwnerCharResponseDto {
    private final String ocid;
    private final String charName;
    private final String worldName;
    private final String charClass;
    private final int charLevel;
    private final long charPower;

    public GetOwnerCharResponseDto(Character character) {
        this.ocid = character.getOcid();
        this.charName = character.getCharName();
        this.worldName = character.getWorldName();
        this.charClass = character.getCharClass();
        this.charLevel = character.getCharLevel();
        this.charPower = character.getCharPower();
    }
}
