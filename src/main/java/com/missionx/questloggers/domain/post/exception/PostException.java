package com.missionx.questloggers.domain.post.exception;

import org.springframework.http.HttpStatus;

public class PostException extends RuntimeException {

  private final HttpStatus status;

  public PostException(HttpStatus status, String message) {
      super(message);
      this.status = status;
  }
}
