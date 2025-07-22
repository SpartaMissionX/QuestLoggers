package com.missionx.questloggers.domain.characterboss.controller;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.characterboss.dto.CreateCharBossResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.MyCharInfoResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.UpdateIsClearedResponseDto;
import com.missionx.questloggers.domain.characterboss.service.CharacterBossService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CharacterBossController {

    private final CharacterBossService characterBossService;

    @PostMapping("/mychar/{charId}/boss/{bossId}")
    public ResponseEntity<ApiResponse<CreateCharBossResponseDto>> createCharBoss(@PathVariable Long charId, @PathVariable Long bossId) {
        CreateCharBossResponseDto responseDto = characterBossService.createCharBoss(charId, bossId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터의 보스 생성 완료", responseDto);
    }

    @GetMapping("/mychar/{charId}")
    public ResponseEntity<ApiResponse<List<MyCharInfoResponseDto>>> myCharInfo(@PathVariable Long charId) {
        List<MyCharInfoResponseDto> responseDtoList = characterBossService.myCharInfo(charId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 정보 조회 완료", responseDtoList);
    }

    @PatchMapping("/mychar/{charId}/boss/{bossId}")
    public ResponseEntity<ApiResponse<UpdateIsClearedResponseDto>> updateIsCleared(@PathVariable Long charId, @PathVariable Long bossId) {
        UpdateIsClearedResponseDto responseDto = characterBossService.updateIsCleared(charId, bossId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 보스 클리어 여부가 변경되었습니다.", responseDto);
    }
}
