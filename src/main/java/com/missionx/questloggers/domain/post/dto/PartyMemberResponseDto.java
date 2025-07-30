package com.missionx.questloggers.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartyMemberResponseDto {
    private Long characterId;
    private String characterName;
}
