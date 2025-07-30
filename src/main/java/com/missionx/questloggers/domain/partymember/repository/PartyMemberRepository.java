package com.missionx.questloggers.domain.partymember.repository;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.partymember.entity.PartyMember;
import com.missionx.questloggers.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    List<PartyMember> findAllByPostId(Long postId);
    Optional<PartyMember> findByPostIdAndCharacterId(Long postId, Long characterId);
}
