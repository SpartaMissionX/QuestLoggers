package com.missionx.questloggers.domain.comment.service;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.domain.comment.dto.*;
import com.missionx.questloggers.domain.comment.entity.Comment;
import com.missionx.questloggers.domain.comment.exception.*;
import com.missionx.questloggers.domain.comment.repository.CommentRepository;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.service.PostService;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.exception.InvalidRequestUserException;
import com.missionx.questloggers.domain.user.service.UserService;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final CharacterService characterService;

    /**
     * 댓글 생성 기능
     */
    @Transactional
    public CreateCommentResponseDto createComment(Long postId, CreateCommentRequestDto requestDto, LoginUser loginUser) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = characterService.findByMainCharId(user.getOwnerCharId());
        Post post = postService.findPostById(postId);

        Comment comment = new Comment(requestDto.getContent(), character, post);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDto(character.getId(), character.getCharName(), savedComment.getId(), savedComment.getContent());
    }

    /**
     * 댓글 조회 기능
     */
    @Transactional(readOnly = true)
    public PageResponseDto<FindAllCommentResponseDto> findAllComment(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Comment> commentsPage = commentRepository.findByPostIdAndDeletedAtNull(postId, pageable);

        if (commentsPage.isEmpty()) {
            throw new CommentException(HttpStatus.NOT_FOUND, "요청한 페이지에 댓글이 존재하지 않습니다.");
        }

        List<FindAllCommentResponseDto> responseDtos = commentsPage.stream()
                .map(comment -> new FindAllCommentResponseDto(comment.getCharacter().getId(), comment.getCharacter().getCharName(), comment.getId(), comment.getContent()))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                responseDtos,
                commentsPage.getNumber() + 1,
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
    public UpdateCommentResponseDto updateComment(Long commentId, UpdateCommentRequestDto requestDto, LoginUser loginUser) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = characterService.findById(user.getOwnerCharId());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다. 다시 확인해주세요"));

        if (!comment.getCharacter().getId().equals(character.getId())) {
            throw new UnauthorizedCommentAccessException("댓글 수정 권한이 없습니다.");
        }

        comment.updateComment(requestDto.getContent());

        return new UpdateCommentResponseDto(comment.getCharacter().getId(), comment.getCharacter().getCharName(), comment.getId(), comment.getContent());
    }

    /**
     * 댓글 삭제 기능
     */
    @Transactional
    public void deleteComment(Long commentId, LoginUser loginUser) {
        User user = userService.findUserById(loginUser.getUserId());
        Character character = characterService.findById(user.getOwnerCharId());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다. 다시 확인해주세요"));
        if (comment.getDeletedAt() == null) {
            comment.delete();
        } else {
            throw new AlreadyDeletedCommentException(HttpStatus.NOT_FOUND , "이미 삭제된 댓글입니다.");
        }

        if (!comment.getCharacter().getId().equals(character.getId())) {
            throw new UnauthorizedCommentAccessException("댓글 삭제 권한이 없습니다.");
        }

        comment.delete();
    }

}
