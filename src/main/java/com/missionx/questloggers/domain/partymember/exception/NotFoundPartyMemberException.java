package com.missionx.questloggers.domain.partymember.exception;

import org.springframework.http.HttpStatus;

public class NotFoundPartyMemberException extends PartyMemberException {
    public NotFoundPartyMemberException(HttpStatus status, String message) {
        super(status, message);
    }
}
