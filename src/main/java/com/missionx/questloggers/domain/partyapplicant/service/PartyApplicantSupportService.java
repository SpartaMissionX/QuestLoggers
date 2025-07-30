package com.missionx.questloggers.domain.partyapplicant.service;

import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import com.missionx.questloggers.domain.partyapplicant.repository.PartyApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartyApplicantSupportService {

    private final PartyApplicantRepository partyApplicantRepository;

    public void save(PartyApplicant partyApplicant) {
        partyApplicantRepository.save(partyApplicant);
    }

    public boolean existsByPostIdAndCharacterId(Long postId, Long id) {
        return partyApplicantRepository.existsByPostIdAndCharacterId(postId, id);
    }

    public List<PartyApplicant> findAllByPostId(Long postId) {
        return partyApplicantRepository.findAllByPostId(postId);
    }
}
