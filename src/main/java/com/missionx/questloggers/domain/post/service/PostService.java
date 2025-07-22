package com.missionx.questloggers.domain.post.service;

import com.missionx.questloggers.domain.post.dto.*;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.exception.NotFoundPostException;
import com.missionx.questloggers.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //포스트 생성 기능
    @Transactional
    public CreatePostResponseDto createPostService(CreatePostRequestDto createPostRequestDto) {
        //임시 유저 아이디
        Long id = 1L;

        String dtoTitle = createPostRequestDto.getTitle();
        String dtoContent = createPostRequestDto.getContent();
        Post newPost = new Post(dtoTitle, dtoContent);
        Post savedPost = postRepository.save(newPost);
        return new CreatePostResponseDto(savedPost.getId(), savedPost.getTitle(), savedPost.getContent());
    }

    //포스트 수정 기능
    @Transactional
    public UpdatePostResponseDto updatePostService(Long postId, UpdatePostRequestDto updatePostRequestDto) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException(HttpStatus.NOT_FOUND, "post not found"));
        foundPost.updatePost(updatePostRequestDto);
        return new UpdatePostResponseDto(foundPost.getId(), foundPost.getTitle(), foundPost.getContent());
    }

    //포스트 다건조회 페이징 및 키워드 검색
    @Transactional(readOnly = true)
    public List<GetAllPostResponseDto> getAllPostService(String keyword, Pageable pageable) {
        Page<Post> foundPostList = postRepository.findByTitleContaining(keyword, pageable);

        return foundPostList.stream()
                .map((post) -> {
                    return new GetAllPostResponseDto(post.getId(), post.getTitle(), post.getContent());
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GetPostResponseDto getPostService(Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("post not found"));

        return new GetPostResponseDto(foundPost.getUser().getId(), foundPost.getId(), foundPost.getTitle(), foundPost.getContent());
    }

    @Transactional
    public void deletePostService(Long postId) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));
        foundPost.delete();
    }

    // 다른 domain에서 사용하는 기능
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException(HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다. 다시 확인해주세요"));
    }
}
