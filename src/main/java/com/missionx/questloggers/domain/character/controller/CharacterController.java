package com.missionx.questloggers.domain.character.controller;

import com.missionx.questloggers.domain.character.dto.*;
import com.missionx.questloggers.domain.character.service.CharacterService;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
     * 캐릭터 생성 API
     */
    @PostMapping("/mychar")
    public ResponseEntity<ApiResponse<AccountListDto>> saveCharList(@AuthenticationPrincipal LoginUser loginUser) {
        characterService.createCharList(loginUser);
        return ApiResponse.success(HttpStatus.OK, "캐릭터 생성이 완료되었습니다.", null);
    }

    /**
     * 본인 캐릭터 조회 API
     */
    @GetMapping("/mychar")
    public ResponseEntity<ApiResponse<List<CharacterListRespnseDto>>> getCharList(@AuthenticationPrincipal LoginUser loginUser) {
        List<CharacterListRespnseDto> charList = characterService.getCharList(loginUser);
        return ApiResponse.success(HttpStatus.OK, "조회가 완료되었습니다.", charList);
    }

    /**
     * 유저 캐릭터 리스트 검색 API
     */
    @GetMapping("/char")
    public ResponseEntity<ApiResponse<List<SerchAllCharResponseDto>>> serchAllChar(
            @RequestParam("keyword") String keyword,
            @PageableDefault(size = 10,page = 0,sort = "charName", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<SerchAllCharResponseDto> responseDtoList = characterService.serchAllCharService(keyword, pageable);
        return ApiResponse.success(HttpStatus.OK,"키워드에 포함된 이름들을 불러왔습니다.", responseDtoList);
    }

    /**
     * 유저 캐릭터 단건 검색 API
     */
    @GetMapping("/char/{charId}")
    public ResponseEntity<ApiResponse<SerchCharResponseDto>> serchChar(@PathVariable Long charId) {
        SerchCharResponseDto serchCharResponseDto = characterService.serchCharService(charId);
        return ApiResponse.success(HttpStatus.FOUND,"캐릭터 정보를 불러왔습니다.",serchCharResponseDto);
    }

    /**
     * 대표캐릭터 설정
     */
    @PostMapping("/mychar/owner/{charId}")
    public ResponseEntity<ApiResponse<SetOwnerCharResponseDto>> setOwnerChar(
            @PathVariable Long charId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        SetOwnerCharResponseDto responseDto = characterService.setOwnerChar(loginUser , charId);
        return ApiResponse.success(HttpStatus.OK, "대표 캐릭터 설정이 완료되었습니다.", responseDto);
    }

    /**
     * 대표캐릭터 업데이트
     */
    @PatchMapping("/mychar/owner/{charId}")
    public ResponseEntity<ApiResponse<UpdateOwnerCharResponseDte>> updateOwnerChar(
            @PathVariable Long charId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        UpdateOwnerCharResponseDte responseDte = characterService.updateOwnerChar(loginUser, charId);
        return ApiResponse.success(HttpStatus.OK, "대표 캐릭터 변경이 완료되었습니다.", responseDte);
    }

}
