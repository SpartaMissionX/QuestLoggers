package com.missionx.questloggers.domain.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetPostResponseDto {
    private final Long postId;
    private final Long userId;
    private final String title;
    private final String content;
}
