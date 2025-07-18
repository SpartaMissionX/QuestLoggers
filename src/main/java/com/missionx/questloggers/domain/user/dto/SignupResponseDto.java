package com.missionx.questloggers.domain.user.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {

    private final int status;
    private final String message;
    private final SignupUserData data;

    public SignupResponseDto(int status, String message, SignupUserData data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @Getter
    public static class SignupUserData {
        private final Long userId;
        private final String email;
        private final String createdAt;

        public SignupUserData(Long userId, String email, String createdAt) {
            this.userId = userId;
            this.email = email;
            this.createdAt = createdAt;
        }
    }

}
