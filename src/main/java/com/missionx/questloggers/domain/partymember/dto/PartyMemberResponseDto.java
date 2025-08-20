package com.missionx.questloggers.domain.partymember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyMemberResponseDto {

    private Long characterId;
    private String characterName;
    private String characterClass;
    private int characterLevel;
    private long characterPower;

}
