package com.missionx.questloggers.domain.partymember.service;

import com.missionx.questloggers.domain.partymember.repository.PartyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartyMemberSupportService {

    private final PartyMemberRepository partyMemberRepository;


}
