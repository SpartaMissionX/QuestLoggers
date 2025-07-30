package com.missionx.questloggers.domain.post.service;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.service.CharacterSupporService;
import com.missionx.questloggers.domain.post.dto.*;
import com.missionx.questloggers.domain.post.entity.PartyApplicant;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.exception.*;
import com.missionx.questloggers.domain.post.repository.PartyApplicantRepository;
import com.missionx.questloggers.domain.post.repository.PostRepository;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserSupporService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserSupporService userSupporService;
    private final PostSupporService postSupporService;
    private final CharacterSupporService characterSupporService;
    private final PartyApplicantRepository partyApplicantRepository;


    /**
     * 게시글 생성
     */
    @Transactional
    public void createPostService(CreatePostRequestDto requestDto, LoginUser loginUser) {
        User user = userSupporService.findUserById(loginUser.getUserId());
        Character ownerCharacter = characterSupporService.findByMainCharId(user.getOwnerCharId());
        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), ownerCharacter, requestDto.getBossId(),
                requestDto.getDifficulty(), requestDto.getPartySize());
        new PartyApplicant(post, ownerCharacter);
        postRepository.save(post);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public UpdatePostResponseDto updatePostService(Long postId, UpdatePostRequestDto updatePostRequestDto, LoginUser loginUser) {
        User user = userSupporService.findUserById(loginUser.getUserId());
        Character ownerCharacter = characterSupporService.findByMainCharId(user.getOwnerCharId());
        Post foundPost = postSupporService.findById(postId);
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
    public PageResponseDto<GetAllPostResponseDto> getAllPostService(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postsPage;
        if (keyword == null) {
            postsPage = postRepository.findByDeletedAtNull(pageable);
        } else {
            postsPage = postRepository.findByTitleContainingAndDeletedAtNull(keyword, pageable);
        }

        if (postsPage.isEmpty()) {
            throw new PostException(HttpStatus.ACCEPTED, "요청한 페이지에 게시글이 존재하지 않습니다.");
        };

        List<GetAllPostResponseDto> responseDtos = postsPage.stream()
                .map(post -> new GetAllPostResponseDto(post.getCharacter().getId(),
                        post.getCharacter().getCharName(),post.getId(), post.getTitle(), post.getContent(),
                        post.getBossId(), post.getDifficulty(), post.getPartySize()))
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
        Post foundPost = postSupporService.findById(postId);
        return new GetPostResponseDto(foundPost.getCharacter().getId(), foundPost.getCharacter().getCharName(),
                foundPost.getId(), foundPost.getTitle(), foundPost.getContent(), foundPost.getBossId(),
                foundPost.getDifficulty(), foundPost.getPartySize());
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePostService(Long postId, LoginUser loginUser) {
        User user = userSupporService.findUserById(loginUser.getUserId());
        Post foundPost = postSupporService.findById(postId);
        if (!foundPost.getCharacter().getId().equals(user.getOwnerCharId())) {
            throw new UnauthorizedPostAccessException("게시글 삭제 권한이 없습니다.");
        }
        foundPost.delete();
    }

    // 파티원 신청
    @Transactional
    public ApplyPartyResponseDto applyPartyResponseDto(Long postId, LoginUser loginUser) {
        User user = userSupporService.findUserById(loginUser.getUserId());
        Character character = characterSupporService.findById(user.getOwnerCharId());
        Post post = postSupporService.findById(postId);
        if (post.getCharacter().getId().equals(character.getId())) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "자신의 파티에는 신청할 수 없습니다.");
        }
        if (partyApplicantRepository.existsByPostIdAndCharacterId(postId, character.getId())) {
            throw new InvalidPartyActionException(HttpStatus.BAD_REQUEST, "이미 신청한 파티입니다.");
        }

        PartyApplicant applicant = new PartyApplicant(post, character);
        partyApplicantRepository.save(applicant);

        return new ApplyPartyResponseDto(post.getId(), character.getId(), character.getCharName());
    }

    // 파티원 신청 조회
    @Transactional
    public List<PartyApplicantResponseDto> getPartyApplicantResponseDto(Long postId, LoginUser loginUser) {
        User user = userSupporService.findUserById(loginUser.getUserId());
        Character character = characterSupporService.findById(user.getOwnerCharId());
        Post post = postRepository.findById(postId).orElseThrow(()-> new NotFoundPostException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        boolean isLeader = post.getCharacter().getId().equals(character.getId());
        boolean isMember = partyApplicantRepository.existsByPostIdAndCharacterId(postId, character.getId());

        if (!isLeader && !isMember) {
            throw new InvalidPartyActionException(HttpStatus.FORBIDDEN, "파티장 또는 파티원만 신청자 목록을 조회할 수 있습니다.");
        }

        List<PartyApplicant> partyApplicants = partyApplicantRepository.findAllByPostId(postId);

        return partyApplicants.stream().map(applicant -> new PartyApplicantResponseDto(
                applicant.getCharacter().getId(), applicant.getCharacter().getCharName(), applicant.getStatus()
        ))
                .collect(Collectors.toList());
    }
}
