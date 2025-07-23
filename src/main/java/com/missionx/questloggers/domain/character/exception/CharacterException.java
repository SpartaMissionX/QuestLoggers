package com.missionx.questloggers.domain.character.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CharacterException extends RuntimeException {
    private final HttpStatus status;
    public CharacterException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
