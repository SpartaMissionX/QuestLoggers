package com.missionx.questloggers.domain.partymember.repository;

import com.missionx.questloggers.domain.partymember.entity.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
}
