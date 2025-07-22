package com.missionx.questloggers.domain.character.controller;

import com.missionx.questloggers.domain.character.dto.GetSerchCharResponseDto;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/char")
    public ResponseEntity<ApiResponse<List<GetSerchCharResponseDto>>> serchChar(
            @RequestParam("keyword") String keyword,
            @PageableDefault(size = 10,page = 0,sort = "charName", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<GetSerchCharResponseDto> responseDtoList = characterService.serchCharService(keyword, pageable);
        return ApiResponse.success(HttpStatus.OK,"키워드에 포함된 이름들을 불러왔습니다.", responseDtoList);
    }

}
