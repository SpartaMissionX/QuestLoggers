package com.missionx.questloggers.domain.user.dto;

import com.missionx.questloggers.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePasswordResponseDto {
    private Long userId;
    private String email;
    private int point;
    private Role role;
    private String token;
}
