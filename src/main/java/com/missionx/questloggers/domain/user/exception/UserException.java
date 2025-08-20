package com.missionx.questloggers.domain.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException {
  public final HttpStatus status;
  public UserException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
