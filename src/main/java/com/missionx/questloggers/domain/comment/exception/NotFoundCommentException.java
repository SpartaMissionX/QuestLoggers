package com.missionx.questloggers.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class NotFoundCommentException extends CommentException {
    public NotFoundCommentException(HttpStatus status, String message) {

        super(status, message);
    }
}
