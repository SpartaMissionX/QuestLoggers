package com.missionx.questloggers.domain.partymember.service;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.partymember.entity.PartyMember;
import com.missionx.questloggers.domain.partymember.exception.NotFoundPartyMemberException;
import com.missionx.questloggers.domain.partymember.repository.PartyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartyMemberSupportService {

    private final PartyMemberRepository partyMemberRepository;

    public void save(PartyMember partyMember) {
        partyMemberRepository.save(partyMember);
    }

    public void delete(PartyMember partyMember) {
        partyMemberRepository.delete(partyMember);
    }

    public List<PartyMember> findAllByPostId(Long postId) {
        return partyMemberRepository.findAllByPostId(postId);
    }

    public PartyMember findByPostIdAndCharacterId(Long postId, Long charId) {
        return partyMemberRepository.findByPostIdAndCharacterId(postId, charId).orElseThrow(
                () -> new NotFoundPartyMemberException(HttpStatus.NOT_FOUND, "파티원을 찾을 수 없습니다.")
        );
    }
}
