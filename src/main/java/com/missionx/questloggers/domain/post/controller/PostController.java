package com.missionx.questloggers.domain.post.controller;

import com.missionx.questloggers.domain.post.dto.*;
import com.missionx.questloggers.domain.post.service.PostService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import com.missionx.questloggers.global.dto.PageResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 생성
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<Object>> createPost(
            @RequestBody @Valid CreatePostRequestDto createPostRequestDto,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        postService.createPostService(createPostRequestDto, loginUser);
        return ApiResponse.success(HttpStatus.CREATED, "게시글 작성이 완료되었습니다.", null);
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<UpdatePostResponseDto>> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDto updatePostRequestDto,
            @AuthenticationPrincipal LoginUser loginUser
            ) {
        UpdatePostResponseDto responseDto = postService.updatePostService(postId, updatePostRequestDto, loginUser);
        return ApiResponse.success(HttpStatus.OK, "게시글 수정이 완료되었습니다.", responseDto);
    }

    /**
     * 게시글 다건 조회 , 검색 , 페이징
     */
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PageResponseDto<GetAllPostResponseDto>>> getAllPost(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponseDto<GetAllPostResponseDto> posts = postService.getAllPostService(keyword, page, size);
        return ApiResponse.success(HttpStatus.ACCEPTED,"게시글 목록 조회가 완료되었습니다.", posts);
    }

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<GetPostResponseDto>> getPost (@PathVariable("postId") Long postId) {
        GetPostResponseDto getPostResponseDto = postService.getPostService(postId);
        return ApiResponse.success(HttpStatus.FOUND,"게시글 조회가 완료되었습니다.", getPostResponseDto);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Object>> deletePost (
            @PathVariable Long postId,
            @AuthenticationPrincipal LoginUser loginUser) {
        postService.deletePostService(postId, loginUser);
        return ApiResponse.success(HttpStatus.OK,"게시글 삭제가 완료되었습니다.", null);
    }

    /**
     * 파티원 신청
     */
    @PostMapping("/posts/{postId}/applicants")
    public ResponseEntity<ApiResponse<ApplyPartyResponseDto>> applyToParty(
            @PathVariable Long postId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        ApplyPartyResponseDto responseDto = postService.applyPartyResponseDto(postId, loginUser);
        return ApiResponse.success(HttpStatus.CREATED, "파티 신청이 완료되었습니다.", responseDto);
    }

    /**
     * 파티원 신청자 조회
     */
    @GetMapping("/posts/{postId}/applicants")
    public ResponseEntity<ApiResponse<List<PartyApplicantResponseDto>>> getPartyApplicants(
            @PathVariable Long postId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        List<PartyApplicantResponseDto> partyApplicantResponseDto = postService.getPartyApplicantResponseDto(postId, loginUser);
        return ApiResponse.success(HttpStatus.OK, "파티 신청자 조회 성공", partyApplicantResponseDto);
    }

    /**
     * 파티 신청 수락
     */
    @PatchMapping("/posts/{postId}/applicants/{charId}")
    public ResponseEntity<ApiResponse<Object>> acceptParty(
            @PathVariable Long postId,
            @PathVariable Long charId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        postService.accpetParty(postId, charId, loginUser);
        return ApiResponse.success(HttpStatus.OK, "파티 신청을 수락했습니다.", null);
    }

    /**
     * 파티 신청 거절
     */
    @DeleteMapping("/posts/{postId}/applicants/{charId}")
    public ResponseEntity<ApiResponse<Object>> rejectParty(
            @PathVariable Long postId,
            @PathVariable Long charId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        postService.rejectParty(postId, charId, loginUser);
        return ApiResponse.success(HttpStatus.OK, "파티 신청을 수락했습니다.", null);
    }
}
