package com.missionx.questloggers.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyApplicantDto {
    private Long characterId;
    private String characterName;
    private String characterClass;
    private int characterLevel;
    private long characterPower;
}
