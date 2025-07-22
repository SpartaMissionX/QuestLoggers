package com.missionx.questloggers.domain.user.dto;

import lombok.Getter;

@Getter
public class UpdatePasswordRequestDto {

    private String currentPassword;
    private String newPassword;
}
