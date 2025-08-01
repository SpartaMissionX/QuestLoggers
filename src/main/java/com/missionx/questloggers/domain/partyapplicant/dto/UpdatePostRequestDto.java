package com.missionx.questloggers.domain.partyapplicant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdatePostRequestDto {

    private String title;
    private String content;
    @Max(value = 6, message = "파티원 수는 최대 6명 입니다.")
    @Min(value = 2, message = "파티원 수는 최소 2명 입니다.")
    private int partySize;
}
