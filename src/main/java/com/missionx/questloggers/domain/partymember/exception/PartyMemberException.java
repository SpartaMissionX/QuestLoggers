package com.missionx.questloggers.domain.partymember.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PartyMemberException extends RuntimeException {
    private final HttpStatus status;
    public PartyMemberException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
