package com.missionx.questloggers.domain.post.dto;

import com.missionx.questloggers.domain.partymember.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyApplicantResponseDto {
    private Long characterId;
    private String characterName;
    private ApplicantStatus status;
}
