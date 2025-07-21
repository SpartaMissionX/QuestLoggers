package com.missionx.questloggers.domain.boss.controller;

import com.missionx.questloggers.domain.boss.dto.CreateBossRequestDto;
import com.missionx.questloggers.domain.boss.dto.CreateBossResponseDto;
import com.missionx.questloggers.domain.boss.service.BossService;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BossController {

    private final BossService bossService;

    @PostMapping("/boss")
    public ResponseEntity<ApiResponse<CreateBossResponseDto>> createBoss(@RequestBody CreateBossRequestDto createBossRequestDto) {
        CreateBossResponseDto responseDto = bossService.createBossService(createBossRequestDto);
        return ApiResponse.success(HttpStatus.CREATED,"보스 생성이 완료되었습니다.",responseDto);
    }
}
