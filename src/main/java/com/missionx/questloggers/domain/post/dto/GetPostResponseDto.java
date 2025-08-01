package com.missionx.questloggers.domain.post.dto;

import com.missionx.questloggers.domain.post.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetPostResponseDto {
    private Long ownerCharId;
    private String ownerCharName;
    private Long postId;
    private String title;
    private String content;
    private Long bossId;
    private String bossName;
    private String bossImage;
    private Difficulty difficulty;
    private int partySize;
}
