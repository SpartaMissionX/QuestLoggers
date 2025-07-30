package com.missionx.questloggers.domain.character.controller;

import com.missionx.questloggers.domain.character.dto.*;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.domain.characterboss.dto.CreateCharBossResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.MyCharInfoResponseDto;
import com.missionx.questloggers.domain.characterboss.dto.UpdateIsClearedResponseDto;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import com.missionx.questloggers.global.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    /**
     * 유저 캐릭터 리스트 검색 API
     */
    @GetMapping("/characters")
    public ResponseEntity<ApiResponse<PageResponseDto<SearchAllCharResponseDto>>> searchAllChar(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponseDto<SearchAllCharResponseDto> responseDto = characterService.serchAllCharService(keyword, page, size);
        return ApiResponse.success(HttpStatus.OK, "키워드에 포함된 이름들을 불러왔습니다.", responseDto);
    }

    /**
     * 유저 캐릭터 단건 검색 API
     */
    @GetMapping("/characters/{charId}")
    public ResponseEntity<ApiResponse<SearchCharResponseDto>> searchChar(@PathVariable Long charId) {
        SearchCharResponseDto searchCharResponseDto = characterService.serchCharService(charId);
        return ApiResponse.success(HttpStatus.FOUND,"캐릭터 정보를 불러왔습니다.", searchCharResponseDto);
    }

    /**
     * 본인 캐릭터 전체 조회 API
     */
    @GetMapping("/characters/me")
    public ResponseEntity<ApiResponse<List<CharacterListRespnseDto>>> getCharList(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        List<CharacterListRespnseDto> charList = characterService.getCharList(loginUser);
        return ApiResponse.success(HttpStatus.OK, "조회가 완료되었습니다.", charList);
    }

    /**
     * 대표캐릭터 설정
     */
    @PostMapping("/characters/me/main")
    public ResponseEntity<ApiResponse<SetOwnerCharResponseDto>> setOwnerChar(
            @PathVariable Long charId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        SetOwnerCharResponseDto responseDto = characterService.setOwnerChar(loginUser , charId);
        return ApiResponse.success(HttpStatus.OK, "대표 캐릭터 설정이 완료되었습니다.", responseDto);
    }

    /**
     * 본인 대표 캐릭터 조회 API
     */
    @GetMapping("/characters/me/main")
    public ResponseEntity<ApiResponse<GetOwnerCharResponseDto>> getOwnerChar(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        GetOwnerCharResponseDto responseDto = characterService.getOwnerChar(loginUser);
        return ApiResponse.success(HttpStatus.OK, "조회가 완료되었습니다.", responseDto);
    }

    /**
     * 대표 캐릭터의 보스 생성
     */
    @PostMapping("/characters/me/main/bosses")
    public ResponseEntity<ApiResponse<CreateCharBossResponseDto>> createCharBoss(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long bossId
    ) {
        CreateCharBossResponseDto responseDto = characterService.createCharBoss(loginUser, bossId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터의 보스 생성 완료", responseDto);
    }

    /**
     * 대표 캐릭터의 보스 클리어 정보 조회
     */
    @GetMapping("/characters/me/main/bosses")
    public ResponseEntity<ApiResponse<List<MyCharInfoResponseDto>>> myCharInfo(
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        List<MyCharInfoResponseDto> responseDtoList = characterService.myCharInfo(loginUser);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 정보 조회 완료", responseDtoList);
    }

    /**
     * 대표 캐릭터의 보스 클리어 여부 수정
     */
    @PatchMapping("/characters/me/main/bosses/{bossId}")
    public ResponseEntity<ApiResponse<UpdateIsClearedResponseDto>> updateIsCleared(
            @AuthenticationPrincipal LoginUser loginUser,
            @PathVariable Long bossId
    ) {
        UpdateIsClearedResponseDto responseDto = characterService.updateIsCleared(loginUser, bossId);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 보스 클리어 여부가 변경되었습니다.", responseDto);
    }
}
