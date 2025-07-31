package com.missionx.questloggers.domain.post.dto;

import com.missionx.questloggers.domain.post.enums.Difficulty;
import com.missionx.questloggers.domain.post.enums.PartySize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    private Difficulty difficulty;
    private PartySize partySize;
}
