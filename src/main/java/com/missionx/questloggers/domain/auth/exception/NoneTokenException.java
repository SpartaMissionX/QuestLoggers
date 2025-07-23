package com.missionx.questloggers.domain.auth.exception;

public class NoneTokenException extends RuntimeException{
    public NoneTokenException(String message) {
        super(message);
    }
}
