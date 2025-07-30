package com.missionx.questloggers.domain.partyapplicant.repository;

import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyApplicantRepository extends JpaRepository<PartyApplicant, Long> {
    boolean existsByPostIdAndCharacterId(Long postId, Long id);
    List<PartyApplicant> findAllByPostId(Long postId);
}
