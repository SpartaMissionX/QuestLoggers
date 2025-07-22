package com.missionx.questloggers.domain.characterboss.exception;

import org.springframework.http.HttpStatus;

public class AlreadyCharacterBossException extends CharacterBossException {
    public AlreadyCharacterBossException(HttpStatus status, String message) {
        super(status, message);
    }
}
