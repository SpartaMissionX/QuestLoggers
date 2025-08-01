package com.missionx.questloggers.domain.post.service;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.boss.service.BossSupportService;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.service.CharacterSupportService;
import com.missionx.questloggers.domain.partyapplicant.dto.UpdatePostRequestDto;
import com.missionx.questloggers.domain.partyapplicant.service.PartyApplicantSupportService;
import com.missionx.questloggers.domain.partymember.dto.KickPartyMemberRequestDto;
import com.missionx.questloggers.domain.partymember.dto.PartyMemberResponseDto;
import com.missionx.questloggers.domain.partymember.entity.PartyMember;
import com.missionx.questloggers.domain.partyapplicant.enums.ApplicantStatus;
import com.missionx.questloggers.domain.partymember.service.PartyMemberSupportService;
import com.missionx.questloggers.domain.post.dto.*;
import com.missionx.questloggers.domain.partyapplicant.entity.PartyApplicant;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.enums.Difficulty;
import com.missionx.questloggers.domain.post.enums.PartySize;
import com.missionx.questloggers.domain.post.exception.*;
import com.missionx.questloggers.domain.post.repository.PostRepository;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserSupportService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserSupportService userSupportService;
    private final PostSupportService postSupportService;
    private final CharacterSupportService characterSupportService;
    private final PartyApplicantSupportService partyApplicantSupportService;
    private final PartyMemberSupportService partyMemberSupportService;
    private final BossSupportService bossSupportService;


    /**
     * 게시글 생성
     */
    @Transactional
    public void createPostService(CreatePostRequestDto requestDto, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character ownerCharacter = characterSupportService.findByMainCharId(user.getOwnerCharId());
        Boss boss = bossSupportService.findById(requestDto.getBossId());
        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), ownerCharacter, boss,
                requestDto.getDifficulty(), requestDto.getPartySize());
        postRepository.save(post);
        partyMemberSupportService.save(new PartyMember(post, ownerCharacter));

    }

    /**
     * 게시글 수정
     */
    @Transactional
    public UpdatePostResponseDto updatePostService(Long postId, UpdatePostRequestDto updatePostRequestDto, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character ownerCharacter = characterSupportService.findByMainCharId(user.getOwnerCharId());
        Post foundPost = postSupportService.findById(postId);
        if (!foundPost.getCharacter().getId().equals(ownerCharacter.getId())) {
            throw new UnauthorizedPostAccessException("게시글 수정 권한이 없습니다.");
        }
        foundPost.updatePost(updatePostRequestDto);
        return new UpdatePostResponseDto(ownerCharacter.getId(), ownerCharacter.getCharName(), foundPost.getId(),
                foundPost.getTitle(), foundPost.getContent(), foundPost.getPartySize());
    }

    /**
     * 게시글 다건 조회 , 검색 , 페이징
     */
    @Transactional(readOnly = true)
    public PageResponseDto<GetAllPostResponseDto> getAllPostService(Long bossId, String difficulty, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postsPage;


        if (bossId == null && difficulty == null) {
            postsPage = postRepository.findByDeletedAtNull(pageable);
        } else if (bossId != null && difficulty == null) {
            postsPage = postRepository.findByBossIdAndDeletedAtNull(bossId, pageable);
        } else if (bossId == null && difficulty != null) {
            Difficulty difficulty1 = Difficulty.valueOf(difficulty);
            postsPage = postRepository.findByDifficultyAndDeletedAtNull(difficulty1, pageable);
        } else {
            Difficulty difficulty1 = Difficulty.valueOf(difficulty);
            postsPage = postRepository.findByBossIdAndDifficultyAndDeletedAtNull(bossId, difficulty1, pageable);
        }

        List<GetAllPostResponseDto> responseDtos = postsPage.stream()
                .map(post -> new GetAllPostResponseDto(post.getCharacter().getId(),
                        post.getCharacter().getCharName(),post.getId(), post.getTitle(),
                        post.getBoss().getId(), post.getBoss().getBossName(), post.getBoss().getBossImage(), post.getDifficulty(), post.getPartySize()))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                responseDtos,
                postsPage.getNumber() +1,
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.isLast()
        );
    }

    /**
     * 게시글 단건 조회
     */
    @Transactional(readOnly = true)
    public GetPostResponseDto getPostService(Long postId) {
        Post foundPost = postSupportService.findById(postId);
        return new GetPostResponseDto(foundPost.getCharacter().getId(), foundPost.getCharacter().getCharName(),
                foundPost.getId(), foundPost.getTitle(), foundPost.getContent(), foundPost.getBoss().getId(),
                foundPost.getBoss().getBossName(), foundPost.getDifficulty(), foundPost.getPartySize());
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePostService(Long postId, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Post foundPost = postSupportService.findById(postId);
        if (!foundPost.getCharacter().getId().equals(user.getOwnerCharId())) {
            throw new UnauthorizedPostAccessException("게시글 삭제 권한이 없습니다.");
        }
        foundPost.delete();
    }

    /**
     * 파티원 신청
     */
    @Transactional
    public ApplyPartyResponseDto applyPartyResponseDto(Long postId, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character character = characterSupportService.findById(user.getOwnerCharId());
        Post post = postSupportService.findById(postId);
        if (post.getCharacter().getId().equals(character.getId())) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "자신의 파티에는 신청할 수 없습니다.");
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

    /**
     * 파티원 신청자 조회
     */
    @Transactional
    public List<PartyApplicantResponseDto> getPartyApplicantResponseDto(Long postId, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character character = characterSupportService.findById(user.getOwnerCharId());
        Post post = postSupportService.findById(postId);

        boolean isLeader = post.getCharacter().getId().equals(character.getId());
        boolean isMember = partyMemberSupportService.existsByPostIdAndCharacterId(postId, character.getId());

        if (!isLeader && !isMember) {
            throw new InvalidPartyActionException(HttpStatus.FORBIDDEN, "파티장 또는 파티원만 신청자 목록을 조회할 수 있습니다.");
        }

        List<PartyApplicant> partyApplicants = partyApplicantSupportService.findAllByPostId(postId);

        return partyApplicants.stream()
                .filter(applicant -> applicant.getStatus() == ApplicantStatus.PENDING)
                .map(applicant -> new PartyApplicantResponseDto(
                        applicant.getCharacter().getId(),
                        applicant.getCharacter().getCharName(),
                        applicant.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * 파티 신청 수락
     */
    @Transactional
    public void accpetParty(Long postId, Long charId, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character leaderCharacter = characterSupportService.findById(user.getOwnerCharId());
        Character applicantCharacter = characterSupportService.findById(charId);
        Post post = postSupportService.findById(postId);
        PartyApplicant partyApplicant = partyApplicantSupportService.findByPostIdAndCharacterId(postId, charId);

        boolean isLeader = post.getCharacter().getId().equals(leaderCharacter.getId());

        if (!isLeader) {
            throw new InvalidPartyActionException(HttpStatus.FORBIDDEN, "파티장만 수락할 수 있습니다.");
        }
        if (partyApplicant.getStatus() == ApplicantStatus.ACCEPTED || partyApplicant.getStatus() == ApplicantStatus.REJECTED) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "이미 수락 또는 거절되었습니다.");
        }

        int currentSize = partyMemberSupportService.findAllByPostId(postId).size();
        if (currentSize >= post.getPartySize().getSize()) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "파티 정원이 모두 찼습니다.");
        }

        partyApplicant.acceptStatus();
        partyMemberSupportService.save(new PartyMember(post,applicantCharacter));
    }

    /**
     * 파티 신청 거절
     */
    @Transactional
    public void rejectParty(Long postId, Long charId, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character leaderCharacter = characterSupportService.findById(user.getOwnerCharId());
        Post post = postSupportService.findById(postId);

        boolean isLeader = post.getCharacter().getId().equals(leaderCharacter.getId());

        PartyApplicant partyApplicant = partyApplicantSupportService.findByPostIdAndCharacterId(postId, charId);

        if (!isLeader) {
            throw new InvalidPartyActionException(HttpStatus.FORBIDDEN, "파티장만 거절할 수 있습니다.");
        }
        if (partyApplicant.getStatus() == ApplicantStatus.ACCEPTED || partyApplicant.getStatus() == ApplicantStatus.REJECTED) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "이미 수락 또는 거절되었습니다.");
        }

        partyApplicant.rejectStatus();

    }

    /**
     * 파티원 조회
     */
    @Transactional(readOnly = true)
    public List<PartyMemberResponseDto> getPartyMembers(Long postId) {
        List<PartyMember> partyMembers = partyMemberSupportService.findAllByPostId(postId);

        return partyMembers.stream().map(partyMember -> new PartyMemberResponseDto(
                        partyMember.getCharacter().getId(), partyMember.getCharacter().getCharName(), partyMember.getCharacter().getCharClass(), partyMember.getCharacter().getCharLevel(), partyMember.getCharacter().getCharPower()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 파티원 추방
     */
    @Transactional
    public void kickPartyMember(Long postId, KickPartyMemberRequestDto requestDto, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character leaderCharacter = characterSupportService.findById(user.getOwnerCharId());
        Post post = postSupportService.findById(postId);

        boolean isLeader = post.getCharacter().getId().equals(leaderCharacter.getId());

        PartyMember partyMember = partyMemberSupportService.findByPostIdAndCharacterId(postId, requestDto.getCharId());
        PartyApplicant partyApplicant = partyApplicantSupportService.findByPostIdAndCharacterId(postId, requestDto.getCharId());

        if (!isLeader) {
            throw new InvalidPartyActionException(HttpStatus.FORBIDDEN, "파티장만 추방할 수 있습니다.");
        }

        partyMemberSupportService.delete(partyMember);
        partyApplicantSupportService.delete(partyApplicant);
    }

    /**
     * 파티 탈퇴
     */
    public void leaveParty(Long postId, LoginUser loginUser) {
        User user = userSupportService.findUserById(loginUser.getUserId());
        Character character = characterSupportService.findById(user.getOwnerCharId());
        Post post = postSupportService.findById(postId);

        boolean isLeader = post.getCharacter().getId().equals(character.getId());

        if (isLeader) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "파티장은 파티를 탈퇴할 수 없습니다.");
        }

        PartyMember partyMember = partyMemberSupportService.findByPostIdAndCharacterId(postId, character.getId());
        PartyApplicant partyApplicant = partyApplicantSupportService.findByPostIdAndCharacterId(postId, character.getId());

        partyMemberSupportService.delete(partyMember);
        partyApplicantSupportService.delete(partyApplicant);

    }
}
