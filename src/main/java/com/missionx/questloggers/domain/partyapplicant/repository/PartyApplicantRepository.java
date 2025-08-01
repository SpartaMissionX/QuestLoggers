package com.missionx.questloggers.domain.partyapplicant.repository;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import com.missionx.questloggers.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyApplicantRepository extends JpaRepository<PartyApplicant, Long> {
    boolean existsByPostIdAndCharacterId(Long postId, Long charId);
    List<PartyApplicant> findAllByPostId(Long postId);
    Optional<PartyApplicant> findByPostIdAndCharacterId(Long postId, Long charId);
}
