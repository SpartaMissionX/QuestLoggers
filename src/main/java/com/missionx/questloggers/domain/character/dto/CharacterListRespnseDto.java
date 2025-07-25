package com.missionx.questloggers.domain.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CharacterListRespnseDto {
    private Long id;
    private String ocid;
    private String charName;
    private String worldName;
    private String charClass;
    private int charLevel;
    private boolean ownerChar;
}
