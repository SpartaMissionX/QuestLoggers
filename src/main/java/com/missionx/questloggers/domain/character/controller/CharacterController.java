package com.missionx.questloggers.domain.character.controller;

import com.missionx.questloggers.domain.character.dto.GetSerchCharResponseDto;
import com.missionx.questloggers.domain.character.dto.AccountListDto;
import com.missionx.questloggers.domain.character.dto.SetOwnerCharResponseDto;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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

    @GetMapping("/mychar")
    public ResponseEntity<ApiResponse<AccountListDto>> getCharList() {
        Long userId = 1L;
        AccountListDto charList = characterService.getCharList(userId);
        return ApiResponse.success(HttpStatus.OK, "조회가 완료되었습니다.", charList);
    }

    @PostMapping("/mychar")
    public ResponseEntity<ApiResponse<AccountListDto>> saveCharList() {
        Long userId = 1L;
        characterService.createCharList(userId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 생성이 완료되었습니다.", null);
    }

    @PostMapping("/mychar/{charId}")
    public void setOwnerChar(@PathVariable Long charId) {
        SetOwnerCharResponseDto responseDto = characterService.setOwnerChar(charId);
        ApiResponse.success(HttpStatus.OK, "대표 캐릭터 설정이 완료되었습니다.", responseDto);
    }
}
