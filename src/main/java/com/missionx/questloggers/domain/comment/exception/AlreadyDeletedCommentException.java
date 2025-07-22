package com.missionx.questloggers.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class AlreadyDeletedCommentException extends CommentException {
    public AlreadyDeletedCommentException(HttpStatus status, String message) {
        super(status, message);
    }
}
