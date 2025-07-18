package com.missionx.questloggers.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final Long userId;
    private final String jwtToken;
}
