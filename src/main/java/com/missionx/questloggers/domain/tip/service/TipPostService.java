package com.missionx.questloggers.domain.tip.service;

import com.missionx.questloggers.domain.tip.dto.CreateTipPostRequestDto;
import com.missionx.questloggers.domain.tip.dto.CreateTipPostResponseDto;
import com.missionx.questloggers.domain.tip.dto.GetAllTipPostResponseDto;
import com.missionx.questloggers.domain.tip.dto.GetTipPostResponseDto;
import com.missionx.questloggers.domain.tip.entity.TipPost;
import com.missionx.questloggers.domain.tip.exception.TipNotFoundException;
import com.missionx.questloggers.domain.tip.repository.TipPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipPostService {

    private final TipPostRepository tipPostRepository;

    @Transactional
    public CreateTipPostResponseDto createTipPost(CreateTipPostRequestDto createTipPostRequestDto) {
        TipPost tipPost = new TipPost(createTipPostRequestDto.getTitle(), createTipPostRequestDto.getContent());
        TipPost saved = tipPostRepository.save(tipPost);

        return new CreateTipPostResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getContent()
        );
    }

    @Transactional(readOnly = true)
    public GetTipPostResponseDto getTipPost(Long tipId) {
        TipPost tipPost = tipPostRepository.findById(tipId)
                .orElseThrow(() ->
                        new TipNotFoundException("해당 팁 게시글이 존재하지 않습니다")
                );

        return new GetTipPostResponseDto(
                tipPost.getId(),
                tipPost.getTitle(),
                tipPost.getContent()
        );
    }

    @Transactional
    public List<GetAllTipPostResponseDto> getAllTipPosts() {
        return tipPostRepository.findAll().stream()
                .map(tipPost -> new GetAllTipPostResponseDto(
                        tipPost.getId(),
                        tipPost.getTitle(),
                        tipPost.getContent()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTipPost(Long tipId) {
        if (!tipPostRepository.existsById(tipId)) {
            throw new TipNotFoundException("해당 팁 게시글이 존재하지 않습니다");
        }
        tipPostRepository.deleteById(tipId);
    }
}
