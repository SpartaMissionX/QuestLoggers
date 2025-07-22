package com.missionx.questloggers.domain.character.controller;

import com.missionx.questloggers.domain.character.dto.AccountListDto;
import com.missionx.questloggers.domain.character.dto.SetOwnerCharResponseDto;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/mychar/{userId}")
    public ResponseEntity<ApiResponse<AccountListDto>> getCharList(@PathVariable Long userId) {
        AccountListDto charList = characterService.getCharList(userId);
        return ApiResponse.success(HttpStatus.OK, "조회가 완료되었습니다.", charList);
    }

    @PostMapping("/mychar/{userId}")
    public ResponseEntity<ApiResponse<AccountListDto>> saveCharList(@PathVariable Long userId) {
        characterService.createCharList(userId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 생성이 완료되었습니다.", null);
    }

    @PostMapping("/mychar/{charId}")
    public void setOwnerChar(@PathVariable Long charId) {
        SetOwnerCharResponseDto responseDto = characterService.setOwnerChar(charId);
        ApiResponse.success(HttpStatus.OK, "대표 캐릭터 설정이 완료되었습니다.", responseDto);
    }
}
