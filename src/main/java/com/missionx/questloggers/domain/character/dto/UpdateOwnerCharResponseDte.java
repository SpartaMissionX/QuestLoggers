package com.missionx.questloggers.domain.character.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateOwnerCharResponseDte {
    private String charName;
    private String worldName;
    private String charClass;
    private Integer charLevel;
}
