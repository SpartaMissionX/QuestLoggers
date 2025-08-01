package com.missionx.questloggers.domain.post.dto;

import com.missionx.questloggers.domain.partyapplicant.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyApplicantResponseDto {
    private Long characterId;
    private String characterName;
    private String characterClass;
    private int characterLevel;
    private long characterPower;
    private ApplicantStatus status;
}
