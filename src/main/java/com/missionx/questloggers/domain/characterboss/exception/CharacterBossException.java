package com.missionx.questloggers.domain.characterboss.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CharacterBossException extends RuntimeException {
    private final HttpStatus status;
    public CharacterBossException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
