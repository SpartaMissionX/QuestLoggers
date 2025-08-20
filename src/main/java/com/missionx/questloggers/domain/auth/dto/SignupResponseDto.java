package com.missionx.questloggers.domain.auth.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private final Long userId;
    private final String email;

    public SignupResponseDto(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
