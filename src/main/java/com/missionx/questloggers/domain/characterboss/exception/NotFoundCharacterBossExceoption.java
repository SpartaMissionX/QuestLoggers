package com.missionx.questloggers.domain.characterboss.exception;

import org.springframework.http.HttpStatus;

public class NotFoundCharacterBossExceoption extends CharacterBossException {
    public NotFoundCharacterBossExceoption(HttpStatus status, String message) {
        super(status, message);
    }
}
