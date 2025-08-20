package com.missionx.questloggers.domain.user.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestUserException extends UserException {
    public InvalidRequestUserException(HttpStatus status, String message) {
        super(status, message);
    }
}
