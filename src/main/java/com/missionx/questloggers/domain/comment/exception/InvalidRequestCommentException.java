package com.missionx.questloggers.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestCommentException extends CommentException {
    public InvalidRequestCommentException(HttpStatus status, String message) {
        super(status, message);
    }
}
