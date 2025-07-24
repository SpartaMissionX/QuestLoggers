package com.missionx.questloggers.domain.character.dto;

import com.missionx.questloggers.domain.character.entity.Character;
import lombok.Getter;

@Getter
public class GetOwnerCharResponseDto {
    private String ocid;
    private String charName;
    private String worldName;
    private String charClass;
    private int charLevel;

    public GetOwnerCharResponseDto(Character character) {
        this.ocid = character.getOcid();
        this.charName = character.getCharName();
        this.worldName = character.getWorldName();
        this.charClass = character.getCharClass();
        this.charLevel = character.getCharLevel();
    }
}
