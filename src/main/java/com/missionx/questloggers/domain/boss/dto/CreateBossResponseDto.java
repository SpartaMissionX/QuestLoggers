package com.missionx.questloggers.domain.boss.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateBossResponseDto {
    private final Long bossId;
    private final String bossName;
    private final String bossImage;
}
