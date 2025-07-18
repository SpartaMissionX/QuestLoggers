package com.missionx.questloggers.domain.post.dto;

import lombok.Getter;

@Getter
public class CreatePostRequestDto {
    private String title;
    private String content;
}
