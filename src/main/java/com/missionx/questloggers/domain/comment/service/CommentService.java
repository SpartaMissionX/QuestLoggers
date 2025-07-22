package com.missionx.questloggers.domain.comment.service;

import com.missionx.questloggers.domain.comment.dto.*;
import com.missionx.questloggers.domain.comment.entity.Comment;
import com.missionx.questloggers.domain.comment.exception.NotFoundCommentException;
import com.missionx.questloggers.domain.comment.repository.CommentRepository;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.service.PostService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserService;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    /**
     * 댓글 생성 기능
     */
    @Transactional
    public CreateCommentResponseDto createComment(Long userId, Long postId, CreateCommentRequestDto requestDto) {
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);

        Comment comment = new Comment(requestDto.getContent(), user, post);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDto(savedComment.getId(), savedComment.getContent());
    }

    /**
     * 댓글 조회 기능
     */
    @Transactional(readOnly = true)
    public PageResponseDto<FindAllCommentResponseDto> findAllComment(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> commentsPage = commentRepository.findByPostIdAndDeletedAtNull(postId, pageable);


        List<FindAllCommentResponseDto> responseDtos = commentsPage.stream()
                .map(comment -> new FindAllCommentResponseDto(comment.getId(), comment.getContent()))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                responseDtos,
                commentsPage.getNumber(),
                commentsPage.getSize(),
                commentsPage.getTotalElements(),
                commentsPage.getTotalPages(),
                commentsPage.isLast()
        );
    }

    /**
     * 댓글 수정 기능
     */
    @Transactional
    public UpdateCommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다. 다시 확인해주세요"));

        comment.updateComment(requestDto.getContent());

        return new UpdateCommentResponseDto(comment.getId(), comment.getContent());
    }

    /**
     * 댓글 삭제 기능
     */
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다. 다시 확인해주세요"));

        comment.delete();
    }
}
