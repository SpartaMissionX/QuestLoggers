package com.missionx.questloggers.domain.notification.enums;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    PARTY_APPLY("파티 신청"),
    PARTY_LEAVE("파티 탈퇴"),
    PARTY_ACCEPT("파티 신청 승인"),
    PARTY_REJECT("파티 신청 거절"),
    PARTY_KICK("파티 추방");


    NotificationStatus(String status) {}
}
