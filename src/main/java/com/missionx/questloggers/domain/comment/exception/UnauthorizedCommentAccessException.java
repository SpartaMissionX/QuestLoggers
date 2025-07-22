package com.missionx.questloggers.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedCommentAccessException extends RuntimeException {

  private final HttpStatus status;

  public UnauthorizedCommentAccessException(String message) {
    super(message);
    this.status = HttpStatus.FORBIDDEN;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
