package com.missionx.questloggers.domain.post.exception;

import org.springframework.http.HttpStatus;

public class AlreadyDeletedPostException extends PostException {
    public AlreadyDeletedPostException(HttpStatus status, String message) {
        super(status, message);
    }
}
