package com.missionx.questloggers.domain.tip.repository;

import com.missionx.questloggers.domain.tip.entity.TipPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipPostRepository extends JpaRepository<TipPost, Long> {
}
