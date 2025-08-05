package com.missionx.questloggers.domain.partyapplicant.service;

import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import com.missionx.questloggers.domain.partyapplicant.exception.NotFoundPartyApplicantException;
import com.missionx.questloggers.domain.partyapplicant.exception.PartyApplicantException;
import com.missionx.questloggers.domain.partyapplicant.repository.PartyApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartyApplicantSupportService {

    private final PartyApplicantRepository partyApplicantRepository;

    public void save(PartyApplicant partyApplicant) {
        partyApplicantRepository.save(partyApplicant);
    }

    public void delete(PartyApplicant partyApplicant) {
        partyApplicantRepository.delete(partyApplicant);
    }

    public boolean existsByPostIdAndCharacterId(Long postId, Long charId) {
        return partyApplicantRepository.existsByPostIdAndCharacterId(postId, charId);
    }

    public List<PartyApplicant> findAllByPostId(Long postId) {
        return partyApplicantRepository.findAllByPostId(postId);
    }

    public PartyApplicant findByPostIdAndCharacterId(Long postId, Long charId) {
        return partyApplicantRepository.findByPostIdAndCharacterId(postId, charId).orElseThrow(
                () -> new NotFoundPartyApplicantException(HttpStatus.NOT_FOUND, "신청한 캐릭터를 찾을 수 없습니다.")
        );
    }

    public void findApplicantCountIsLimit(Long postId) {
        List<PartyApplicant> partyApplicantList = partyApplicantRepository.findAllByPostId(postId);
        int count = partyApplicantList.size();
        if (count >= 100) {
            throw new PartyApplicantException(HttpStatus.TOO_MANY_REQUESTS, "신청 인원이 100 명을 넘었습니다.");
        }
    }
}
