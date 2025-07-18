package com.missionx.questloggers.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class CommentException extends RuntimeException {

  private final HttpStatus status;

  public CommentException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }
}
