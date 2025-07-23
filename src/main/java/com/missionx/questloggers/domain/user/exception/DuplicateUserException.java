package com.missionx.questloggers.domain.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class DuplicateUserException extends UserException {
    public DuplicateUserException(HttpStatus status, String message) {
        super(status, message);
    }
}