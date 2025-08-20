package com.missionx.questloggers.domain.post.service;

import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.exception.NotFoundPostException;
import com.missionx.questloggers.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSupportService {

    private final PostRepository postRepository;

    // 다른 domain에서 사용하는 기능
    @Transactional
    public Post findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundPostException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다. 다시 확인해주세요.")
        );
        if (post.getDeletedAt() != null) {
            throw new NotFoundPostException(HttpStatus.NOT_FOUND, "삭제된 게시글 입니다. 다시 확인해주세요.");
        }
        return post;
    }
}
