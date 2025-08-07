package com.missionx.questloggers.domain.test.party;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.repository.CharacterRepository;
import com.missionx.questloggers.domain.character.service.CharacterSupportService;
import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import com.missionx.questloggers.domain.partyapplicant.enums.ApplicantStatus;
import com.missionx.questloggers.domain.partyapplicant.service.PartyApplicantSupportService;
import com.missionx.questloggers.domain.post.dto.ApplyPartyResponseDto;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.exception.InvalidPartyActionException;
import com.missionx.questloggers.domain.post.service.PostSupportService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestApplyPartyService {

    private final PostSupportService postSupportService;
    private final PartyApplicantSupportService partyApplicantSupportService;
    private final CharacterSupportService characterSupportService;
    private final UserSupportService userSupportService;
    private final CharacterRepository characterRepository;

    @Transactional
    public ApplyPartyResponseDto applyPartyResponseDto(Long userId) {
        Long postId = 1L;
        User foundUser = userSupportService.findUserById(userId);
        Character character = characterSupportService.findById(foundUser.getOwnerCharId());
        Post post = postSupportService.findById(postId);
        if (post.getCharacter().getId().equals(character.getId())) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "자신의 파티에는 신청할 수 없습니다.");
        }

        partyApplicantSupportService.findApplicantCountIsLimit(postId);

        List<Character> characterList1 = characterSupportService.findByUser(foundUser);
        for (Character c : characterList1) {
            if (partyApplicantSupportService.existsByPostIdAndCharacterId(postId, c.getId())) {
                PartyApplicant partyApplicant = partyApplicantSupportService.findByPostIdAndCharacterId(postId, c.getId());
                if (partyApplicant.getStatus() == ApplicantStatus.ACCEPTED || partyApplicant.getStatus() == ApplicantStatus.PENDING) {
                    throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "이미 본인의 다른 캐릭터가 파티 멤버이거나 파티 신청중입니다.");
                }
            }
        }
        if (partyApplicantSupportService.existsByPostIdAndCharacterId(postId, character.getId())) {
            if (partyApplicantSupportService.findByPostIdAndCharacterId(postId, character.getId()).getStatus() == ApplicantStatus.ACCEPTED) {
                throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "이미 수락된 파티입니다.");
            } else if (partyApplicantSupportService.findByPostIdAndCharacterId(postId, character.getId()).getStatus() == ApplicantStatus.PENDING) {
                throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "이미 신청한 파티입니다.");
            } else {
                PartyApplicant partyApplicant = partyApplicantSupportService.findByPostIdAndCharacterId(postId, character.getId());
                partyApplicant.pendingStatus();
            }
        } else {
            PartyApplicant applicant = new PartyApplicant(post, character);
            partyApplicantSupportService.save(applicant);
        }

        return new ApplyPartyResponseDto(post.getId(), character.getId(), character.getCharName());
    }
}
