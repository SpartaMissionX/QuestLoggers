package com.missionx.questloggers.domain.partyapplicant.dto;

import com.missionx.questloggers.domain.post.enums.PartySize;
import lombok.Getter;

@Getter
public class UpdatePostRequestDto {
    private String title;
    private String content;
    private PartySize partySize;
}
