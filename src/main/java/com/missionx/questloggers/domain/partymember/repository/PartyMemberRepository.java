package com.missionx.questloggers.domain.partymember.repository;

import com.missionx.questloggers.domain.partymember.entity.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    List<PartyMember> findAllByPostId(Long postId);
}
