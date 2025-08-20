package com.missionx.questloggers.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class FindAllCommentResponseDto {
    private Long ownerCharId;
    private String ownerCharName;
    private Long id;
    private String content;
}
