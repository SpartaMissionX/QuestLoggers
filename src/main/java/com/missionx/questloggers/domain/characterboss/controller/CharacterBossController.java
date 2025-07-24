package com.missionx.questloggers.domain.characterboss.controller;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.characterboss.dto.CreateCharBossResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.MyCharInfoResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.UpdateIsClearedResponseDto;
import com.missionx.questloggers.domain.characterboss.service.CharacterBossService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CharacterBossController {

    private final CharacterBossService characterBossService;

    /**
     * 대표 캐릭터의 보스 생성
     */
    @PostMapping("/mychar/owner/boss/{bossId}")
    public ResponseEntity<ApiResponse<CreateCharBossResponseDto>> createCharBoss(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long bossId
    ) {
        CreateCharBossResponseDto responseDto = characterBossService.createCharBoss(loginUser, bossId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터의 보스 생성 완료", responseDto);
    }

    /**
     * 대표 캐릭터의 보스 클리어 정보 조회
     */
    @GetMapping("/mychar/owner/boss")
    public ResponseEntity<ApiResponse<List<MyCharInfoResponseDto>>> myCharInfo(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        List<MyCharInfoResponseDto> responseDtoList = characterBossService.myCharInfo(loginUser);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 정보 조회 완료", responseDtoList);
    }

    /**
     * 대표 캐릭터의 보스 클리어 여부 수정
     */
    @PatchMapping("/mychar/owner/boss/{bossId}")
    public ResponseEntity<ApiResponse<UpdateIsClearedResponseDto>> updateIsCleared(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long bossId
    ) {
        UpdateIsClearedResponseDto responseDto = characterBossService.updateIsCleared(loginUser, bossId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 보스 클리어 여부가 변경되었습니다.", responseDto);
    }
}
