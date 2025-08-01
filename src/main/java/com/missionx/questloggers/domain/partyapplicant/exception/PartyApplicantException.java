package com.missionx.questloggers.domain.partyapplicant.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PartyApplicantException extends RuntimeException {
    private final HttpStatus status;
    public PartyApplicantException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
