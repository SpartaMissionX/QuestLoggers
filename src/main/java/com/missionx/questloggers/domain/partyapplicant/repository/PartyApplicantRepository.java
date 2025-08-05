package com.missionx.questloggers.domain.partyapplicant.repository;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import com.missionx.questloggers.domain.post.entity.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface PartyApplicantRepository extends JpaRepository<PartyApplicant, Long> {
    boolean existsByPostIdAndCharacterId(Long postId, Long charId);
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<PartyApplicant> findAllByPostId(Long postId);
    Optional<PartyApplicant> findByPostIdAndCharacterId(Long postId, Long charId);
}
