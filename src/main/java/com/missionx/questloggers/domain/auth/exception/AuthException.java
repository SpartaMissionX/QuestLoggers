package com.missionx.questloggers.domain.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {
  private final HttpStatus status;

  public AuthException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
