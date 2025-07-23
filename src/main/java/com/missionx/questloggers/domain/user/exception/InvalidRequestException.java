package com.missionx.questloggers.domain.user.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends UserException {
    public InvalidRequestException(HttpStatus status, String message) {
        super(status, message);
    }
}
