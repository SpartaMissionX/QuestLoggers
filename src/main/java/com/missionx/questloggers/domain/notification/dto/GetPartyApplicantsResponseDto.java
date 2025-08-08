package com.missionx.questloggers.domain.notification.dto;

import com.missionx.questloggers.domain.partyapplicant.enums.ApplicantStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetPartyApplicantsResponseDto<T> {

    private final String massage;
    private final ApplicantStatus applicantStatus;
    private final T data;


}