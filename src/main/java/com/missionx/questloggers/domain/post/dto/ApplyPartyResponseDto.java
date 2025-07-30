package com.missionx.questloggers.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplyPartyResponseDto {
    private Long postId;
    private Long characterId;
    private String characterName;
}