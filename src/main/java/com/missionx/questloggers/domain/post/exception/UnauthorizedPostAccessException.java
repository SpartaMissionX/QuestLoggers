package com.missionx.questloggers.domain.post.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedPostAccessException extends RuntimeException {

    private final HttpStatus status;

    public UnauthorizedPostAccessException(String message) {
        super(message);
        this.status = HttpStatus.FORBIDDEN;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
