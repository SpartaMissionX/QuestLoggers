package com.missionx.questloggers.domain.boss.exception;

import org.springframework.http.HttpStatus;

public class NotFoundBossException extends BossException {
    public NotFoundBossException(HttpStatus status, String message) {
        super(status,message);
    }
}
