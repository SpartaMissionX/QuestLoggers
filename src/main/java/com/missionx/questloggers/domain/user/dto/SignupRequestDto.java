package com.missionx.questloggers.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private String email;
    private String password;
    private String apiKey;

    public SignupRequestDto(String email, String password, String apiKey) {
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
    }
}
