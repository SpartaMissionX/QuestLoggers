package com.missionx.questloggers.global.config.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    TIP_SINGLE("tipSingle", 10*60, 100),
    TIP_ALL("tipAll", 10*60, 1);

    private final String cacheName;
    private final int expireAfterWriteSec;
    private final int maximumSize;

}
