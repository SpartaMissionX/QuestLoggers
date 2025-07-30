package com.missionx.questloggers.domain.post.exception;

import org.springframework.http.HttpStatus;

public class InvalidPartyActionException extends PostException {
    public InvalidPartyActionException(HttpStatus status, String message) {
        super(status, message);
    }
}
