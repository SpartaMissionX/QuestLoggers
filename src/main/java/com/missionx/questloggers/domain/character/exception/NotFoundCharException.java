package com.missionx.questloggers.domain.character.exception;

import com.missionx.questloggers.domain.comment.exception.CommentException;
import org.springframework.http.HttpStatus;

public class NotFoundCharException extends CommentException {
    public NotFoundCharException(HttpStatus status, String message) {
        super(status, message);
    }
}
