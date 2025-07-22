package com.missionx.questloggers.domain.characterboss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateIsClearedResponseDto {
    private Long charId;
    private Long bossId;
    private boolean isCleared;
    private int clearCount;
}
