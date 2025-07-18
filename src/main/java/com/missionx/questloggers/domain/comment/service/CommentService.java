package com.missionx.questloggers.domain.comment.service;

import com.missionx.questloggers.domain.comment.dto.CreateCommentRequestDto;
import com.missionx.questloggers.domain.comment.dto.CreateCommentResponseDto;
import com.missionx.questloggers.domain.comment.dto.UpdateCommentRequestDto;
import com.missionx.questloggers.domain.comment.dto.UpdateCommentResponseDto;
import com.missionx.questloggers.domain.comment.entity.Comment;
import com.missionx.questloggers.domain.comment.exception.NotFoundCommentException;
import com.missionx.questloggers.domain.comment.repository.CommentRepository;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.service.PostService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public CreateCommentResponseDto createComment(Long userId, Long postId, CreateCommentRequestDto requestDto) {
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);

        Comment comment = new Comment(requestDto.getContent(), user, post);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDto(savedComment.getId(), savedComment.getContent());
    }

    @Transactional
    public UpdateCommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다. 다시 확인해주세요"));

        comment.updateComment(requestDto.getContent());

        return new UpdateCommentResponseDto(comment.getId(), comment.getContent());
    }
}
