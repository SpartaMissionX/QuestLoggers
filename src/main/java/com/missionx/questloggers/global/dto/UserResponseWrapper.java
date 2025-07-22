package com.missionx.questloggers.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseWrapper<T> {
    private String message;
    private T data;
}
