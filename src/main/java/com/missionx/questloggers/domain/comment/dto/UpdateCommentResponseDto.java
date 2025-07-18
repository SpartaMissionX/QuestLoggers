package com.missionx.questloggers.domain.comment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateCommentResponseDto {

    private final Long id;
    private final String content;

}
