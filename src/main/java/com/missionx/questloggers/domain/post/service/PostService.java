package com.missionx.questloggers.domain.post.service;

import com.missionx.questloggers.domain.post.dto.CreatePostRequestDto;
import com.missionx.questloggers.domain.post.dto.CreatePostResponseDto;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.exception.NotFoundPostException;
import com.missionx.questloggers.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public CreatePostResponseDto createPostService(CreatePostRequestDto createPostRequestDto) {
        //임시 유저 아이디
        Long id = 1L;

        String dtoTitle = createPostRequestDto.getTitle();
        String dtoContent = createPostRequestDto.getContent();
        Post newPost = new Post(dtoTitle, dtoContent);
        Post savedPost = postRepository.save(newPost);
        CreatePostResponseDto createPostResponseDto = new CreatePostResponseDto(savedPost.getId(), savedPost.getTitle(), savedPost.getContent());
        return createPostResponseDto;
    }


    // 다른 domain에서 사용하는 기능
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundPostException(HttpStatus.NOT_FOUND,
                        "게시글을 찾을 수 없습니다. 다시 확인해주세요"));
    }
}
