package com.missionx.questloggers.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class NoneTokenException extends AuthException {
    public NoneTokenException(HttpStatus status, String message) {
        super(status, message);
    }
}
