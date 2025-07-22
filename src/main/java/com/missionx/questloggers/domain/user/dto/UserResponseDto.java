package com.missionx.questloggers.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private String apiKey;

    public UserResponseDto(Long userId, String email, String apiKey) {
        this.userId = userId;
        this.email = email;
        this.apiKey = apiKey;
    }
}
