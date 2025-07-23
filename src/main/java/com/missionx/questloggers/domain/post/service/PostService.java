package com.missionx.questloggers.domain.post.service;

import com.missionx.questloggers.domain.post.dto.*;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.exception.AlreadyDeletedPostException;
import com.missionx.questloggers.domain.post.exception.NotFoundPostException;
import com.missionx.questloggers.domain.post.exception.PostException;
import com.missionx.questloggers.domain.post.exception.UnauthorizedPostAccessException;
import com.missionx.questloggers.domain.post.repository.PostRepository;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.NotFoundUserException;
import com.missionx.questloggers.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    /**
     * 게시글 생성
     */
    @Transactional
    public CreatePostResponseDto createPostService(CreatePostRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), user);
        postRepository.save(post);

        return new CreatePostResponseDto(post.getId(), post.getTitle(), post.getContent());
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public UpdatePostResponseDto updatePostService(Long postId, UpdatePostRequestDto updatePostRequestDto, Long userId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        if (!foundPost.getUser().getId().equals(userId)) {
            throw new UnauthorizedPostAccessException("게시글 수정 권한이 없습니다.");
        }
        foundPost.updatePost(updatePostRequestDto);
        return new UpdatePostResponseDto(foundPost.getId(), foundPost.getTitle(), foundPost.getContent());
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
            throw new PostException(HttpStatus.NOT_FOUND, "요청한 페이지에 게시글이 존재하지 않습니다.");
        };

        List<GetAllPostResponseDto> responseDtos = postsPage.stream()
                .map(post -> new GetAllPostResponseDto(post.getId(), post.getTitle(), post.getContent()))
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
        Post foundPost = postRepository.findByIdAndDeletedAtNull(postId)
                .orElseThrow(()-> new RuntimeException("게시글을 찾을 수 없습니다."));

        return new GetPostResponseDto(foundPost.getUser().getId(), foundPost.getId(), foundPost.getTitle(), foundPost.getContent());
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePostService(Long postId, Long userId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        if (foundPost.getDeletedAt() == null) {
            foundPost.delete();
        } else {
            throw new AlreadyDeletedPostException(HttpStatus.NOT_FOUND, "이미 삭제된 게시글입니다.");
        }
        if (!foundPost.getUser().getId().equals(userId)) {
            throw new UnauthorizedPostAccessException("게시글 삭제 권한이 없습니다.");
        }
        foundPost.delete();
    }

    // 다른 domain에서 사용하는 기능
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException(HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다. 다시 확인해주세요"));
    }
}
