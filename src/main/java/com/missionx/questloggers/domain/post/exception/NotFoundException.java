package com.missionx.questloggers.domain.post.exception;

import com.missionx.questloggers.domain.post.entity.Post;
import org.springframework.http.HttpStatus;

public class NotFoundException extends PostException {
    public NotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
