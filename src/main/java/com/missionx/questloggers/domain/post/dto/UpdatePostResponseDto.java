package com.missionx.questloggers.domain.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdatePostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
}
