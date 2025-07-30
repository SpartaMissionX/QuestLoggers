package com.missionx.questloggers.domain.post.enums;

import lombok.Getter;

@Getter
public enum PartySize {
    TWO("2인"), THREE("3인"), FOUR("4인"), FIVE("5인"), SIX("6인");

    private final String displayName;

    PartySize(String displayName) {
        this.displayName = displayName;
    }

}