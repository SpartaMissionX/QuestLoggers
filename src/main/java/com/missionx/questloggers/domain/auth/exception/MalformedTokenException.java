package com.missionx.questloggers.domain.auth.exception;

public class MalformedTokenException extends RuntimeException {
    public MalformedTokenException(String message) {
        super(message);
    }
}