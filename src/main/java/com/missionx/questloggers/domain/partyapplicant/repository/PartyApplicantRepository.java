package com.missionx.questloggers.domain.partyapplicant.repository;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import com.missionx.questloggers.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyApplicantRepository extends JpaRepository<PartyApplicant, Long> {
    boolean existsByPostIdAndCharacterId(Long postId, Long charId);
    List<PartyApplicant> findAllByPostId(Long postId);
    PartyApplicant findByPostIdAndCharacterId(Long postId, Long charId);
}
