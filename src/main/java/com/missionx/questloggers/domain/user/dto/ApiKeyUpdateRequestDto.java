package com.missionx.questloggers.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public class ApiKeyUpdateRequestDto {
    @NotBlank(message = "새 API 키를 입력해주세요.")
    private String newApiKey;
}
