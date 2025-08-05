package com.missionx.questloggers.domain.tip.exception;

public class TipNotFoundException extends RuntimeException {
    public TipNotFoundException(String message) {
        super(message);
    }
}