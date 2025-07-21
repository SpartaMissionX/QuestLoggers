package com.missionx.questloggers.domain.boss.service;

import com.missionx.questloggers.domain.boss.dto.CreateBossRequestDto;
import com.missionx.questloggers.domain.boss.dto.CreateBossResponseDto;
import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.repository.BossRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BossService {

    public final BossRepository bossRepository;

    @Transactional
    public CreateBossResponseDto createBossService(CreateBossRequestDto createBossRequestDto) {
        Boss newBoss = new Boss(createBossRequestDto);
        Boss savedBoss = bossRepository.save(newBoss);
        return new CreateBossResponseDto(savedBoss.getId(), savedBoss.getBossName(), savedBoss.getBossImage());
    }

}
