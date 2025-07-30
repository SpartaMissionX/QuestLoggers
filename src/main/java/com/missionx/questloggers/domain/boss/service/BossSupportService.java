package com.missionx.questloggers.domain.boss.service;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.exception.NotFoundBossException;
import com.missionx.questloggers.domain.boss.repository.BossRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BossSupportService {

    private final BossRepository bossRepository;

    /**
     * 다른 domain 에서 사용
     */
    @Transactional
    public Boss findById(Long bossId) {
        return bossRepository.findById(bossId).orElseThrow(
                () -> new NotFoundBossException(HttpStatus.NOT_FOUND, "보스를 찾을 수 없습니다.")
        );
    }
}
