package com.missionx.questloggers.domain.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class SearchAllCharResponseDto {
    private Long charId;
    private String charName;
    private String worldName;
    private String charClass;
    private int charLevel;
}
