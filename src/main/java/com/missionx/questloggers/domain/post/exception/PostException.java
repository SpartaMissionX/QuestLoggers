package com.missionx.questloggers.domain.post.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostException extends RuntimeException {
  private final HttpStatus status;
  public PostException(HttpStatus status, String message) {
      super(message);
      this.status = status;
  }
}
