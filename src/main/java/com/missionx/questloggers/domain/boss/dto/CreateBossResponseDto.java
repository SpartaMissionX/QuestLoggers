package com.missionx.questloggers.domain.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBossResponseDto {
    private Long bossId;
    private String bossName;
    private String bossImage;
}
