package com.missionx.questloggers.domain.partymember.service;

import com.missionx.questloggers.domain.partymember.entity.PartyMember;
import com.missionx.questloggers.domain.partymember.repository.PartyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartyMemberSupportService {

    private final PartyMemberRepository partyMemberRepository;

    public void save(PartyMember partyMember) {
        partyMemberRepository.save(partyMember);
    }

    public List<PartyMember> findAllByPostId(Long postId) {
        return partyMemberRepository.findAllByPostId(postId);
    }
}
