package com.missionx.questloggers.domain.tip.service;

import com.missionx.questloggers.domain.tip.dto.CreateTipPostRequestDto;
import com.missionx.questloggers.domain.tip.dto.CreateTipPostResponseDto;
import com.missionx.questloggers.domain.tip.dto.GetAllTipPostResponseDto;
import com.missionx.questloggers.domain.tip.dto.GetTipPostResponseDto;
import com.missionx.questloggers.domain.tip.entity.TipPost;
import com.missionx.questloggers.domain.tip.exception.TipNotFoundException;
import com.missionx.questloggers.domain.tip.repository.TipPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipPostService {

    private final TipPostRepository tipPostRepository;

    @CacheEvict(value = {"tipSingle", "tipAll"}, allEntries = true)
    @Transactional
    public CreateTipPostResponseDto createTipPost(CreateTipPostRequestDto dto) {
        TipPost saved = tipPostRepository.save(
                new TipPost(dto.getTitle(), dto.getContent())
        );

        return new CreateTipPostResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getContent()
        );
    }

    @Cacheable(value = "tipSingle", key = "#tipId")
    @Transactional(readOnly = true)
    public GetTipPostResponseDto getTipPost(Long tipId) {
        TipPost tipPost = tipPostRepository.findById(tipId)
                .orElseThrow(() -> new TipNotFoundException("해당 팁 게시글이 존재하지 않습니다"));

        return new GetTipPostResponseDto(
                tipPost.getId(),
                tipPost.getTitle(),
                tipPost.getContent()
        );
    }

    @Cacheable(value = "tipAll", key = "'all'")
    @Transactional(readOnly = true)
    public List<GetAllTipPostResponseDto> getAllTipPosts() {
        return tipPostRepository.findAll().stream()
                .map(t -> new GetAllTipPostResponseDto(
                        t.getId(),
                        t.getTitle(),
                        t.getContent()
                ))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"tipSingle", "tipAll"}, allEntries = true)
    @Transactional
    public void deleteTipPost(Long tipId) {
        if (!tipPostRepository.existsById(tipId)) {
            throw new TipNotFoundException("해당 팁 게시글이 존재하지 않습니다");
        }
        tipPostRepository.deleteById(tipId);
    }
}
