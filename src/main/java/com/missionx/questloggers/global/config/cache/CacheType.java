package com.missionx.questloggers.global.config.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    // 5분 300개
    TIP_SINGLE("tipSingle", 5*60, 300),
    // 5분 1개
    TIP_ALL("tipAll", 5*60, 1);

    private final String cacheName;
    private final int expireAfterWriteSec;
    private final int maximumSize;

}
