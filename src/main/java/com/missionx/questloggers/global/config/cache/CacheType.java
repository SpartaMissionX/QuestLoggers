package com.missionx.questloggers.global.config.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    TIP_SINGLE("tipSingle", 24 * 60 * 60, 2000),
    TIP_ALL("tipAll",24 * 60 * 60, 2000);

    private final String cacheName;
    private final int expireAfterWriteSec;
    private final int maximumSize;

}
