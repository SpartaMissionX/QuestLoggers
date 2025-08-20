package com.missionx.questloggers.domain.user.exception;

import org.springframework.http.HttpStatus;

public class NotFoundUserException extends UserException {
    public NotFoundUserException(HttpStatus status, String message) {
        super(status, message);
    }
}
