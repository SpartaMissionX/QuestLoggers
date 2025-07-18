package com.missionx.questloggers.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final Long userId;
    private final String jwtToken;

    public LoginResponseDto(Long userId, String jwtToken) {
        this.userId = userId;
        this.jwtToken = jwtToken;
    }
}
