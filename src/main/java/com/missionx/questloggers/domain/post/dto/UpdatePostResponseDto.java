package com.missionx.questloggers.domain.post.dto;

import com.missionx.questloggers.domain.post.enums.PartySize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class UpdatePostResponseDto {
    private Long OwnerCharId;
    private String OwnerCharName;
    private Long id;
    private String title;
    private String content;
    private PartySize partySize;
}
