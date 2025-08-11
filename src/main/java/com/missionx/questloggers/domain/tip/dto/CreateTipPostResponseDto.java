package com.missionx.questloggers.domain.tip.dto;

import lombok.Getter;

@Getter
public class CreateTipPostResponseDto {
    private Long id;
    private String title;
    private String content;

    public CreateTipPostResponseDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
