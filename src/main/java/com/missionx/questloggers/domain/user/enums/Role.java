package com.missionx.questloggers.domain.user.enums;

import com.missionx.questloggers.domain.user.exception.InvalidRequestUserException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum Role {

    USER, ADMIN;

    public static Role of(String role) {
        return Arrays.stream(Role.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestUserException(HttpStatus.BAD_REQUEST, "유효하지 않은 권한"));
    }
}
