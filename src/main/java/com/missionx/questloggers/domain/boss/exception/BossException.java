package com.missionx.questloggers.domain.boss.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BossException extends RuntimeException {
    private final HttpStatus status;
    public BossException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
