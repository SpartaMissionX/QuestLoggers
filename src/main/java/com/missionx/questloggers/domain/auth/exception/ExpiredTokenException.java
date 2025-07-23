package com.missionx.questloggers.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends AuthException {
  public ExpiredTokenException(HttpStatus status, String message) {
    super(status, message);
  }
}
