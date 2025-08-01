package com.missionx.questloggers.domain.partyapplicant.exception;

import org.springframework.http.HttpStatus;

public class NotFoundPartyApplicantException extends PartyApplicantException {
    public NotFoundPartyApplicantException(HttpStatus status, String message) {
        super(status, message);
    }
}
