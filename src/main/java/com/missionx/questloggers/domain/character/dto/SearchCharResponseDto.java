package com.missionx.questloggers.domain.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchCharResponseDto {
    private Long Id;
    private String charName;
    private String worldName;
    private String charClass;
    private int charLevel;
    private long charPower;

}
