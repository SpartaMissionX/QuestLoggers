package com.missionx.questloggers.domain.character.exception;

import org.springframework.http.HttpStatus;

public class AlreadyHaveOwnerCharacterException extends CharacterException {
    public AlreadyHaveOwnerCharacterException(HttpStatus status, String message) {
        super(status, message);
    }
}
