package com.missionx.questloggers.domain.post.enums;

import lombok.Getter;

@Getter
public enum PartySize {
    TWO(2, "2인"), THREE(3, "3인"), FOUR(4, "4인"), FIVE(5, "5인"), SIX(6, "6인");

    private final String displayName;
    private final int size;

    PartySize(int size, String displayName) {
        this.size = size;
        this.displayName = displayName;
    }
}