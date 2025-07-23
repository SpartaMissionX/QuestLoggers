package com.missionx.questloggers.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class MalformedTokenException extends AuthException {
    public MalformedTokenException(HttpStatus status, String message) {
        super(status, message);
    }
}
