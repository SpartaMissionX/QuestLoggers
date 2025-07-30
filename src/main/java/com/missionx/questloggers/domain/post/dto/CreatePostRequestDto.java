package com.missionx.questloggers.domain.post.dto;

import com.missionx.questloggers.domain.post.enums.Difficulty;
import com.missionx.questloggers.domain.post.enums.PartySize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreatePostRequestDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "보스 ID는 필수입니다.")
    private Long bossId;

    @NotNull(message = "난이도는 필수입니다.")
    private Difficulty difficulty;

    @NotNull(message = "파티원 수는 필수입니다.")
    private PartySize partySize;
}
