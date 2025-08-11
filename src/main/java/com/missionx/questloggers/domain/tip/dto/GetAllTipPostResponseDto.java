package com.missionx.questloggers.domain.tip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllTipPostResponseDto {
    private Long id;
    private String title;
    private String content;
}
