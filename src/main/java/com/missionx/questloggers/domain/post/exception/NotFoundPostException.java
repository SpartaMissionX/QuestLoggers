package com.missionx.questloggers.domain.post.exception;

import org.springframework.http.HttpStatus;

public class NotFoundPostException extends PostException {
    public NotFoundPostException(HttpStatus status, String message) {
        super(status, message);
    }
}
