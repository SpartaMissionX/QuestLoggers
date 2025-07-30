package com.missionx.questloggers.domain.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class GetAllBossResponseDto {
    private Long bossId;
    private String bossName;
    private String bossImage;
}
