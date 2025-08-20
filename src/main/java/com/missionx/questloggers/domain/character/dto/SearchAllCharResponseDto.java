package com.missionx.questloggers.domain.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchAllCharResponseDto {
    private Long charId;
    private String charName;
    private String worldName;
    private int charLevel;
}
