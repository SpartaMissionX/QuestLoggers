package com.missionx.questloggers.domain.boss.repository;

import com.missionx.questloggers.domain.boss.entity.Boss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BossRepository extends JpaRepository<Boss, Long> {
    boolean existsByBossName(String bossName);
}