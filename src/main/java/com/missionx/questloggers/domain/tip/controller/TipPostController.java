package com.missionx.questloggers.domain.tip.controller;

import com.missionx.questloggers.domain.tip.dto.CreateTipPostRequestDto;
import com.missionx.questloggers.domain.tip.dto.CreateTipPostResponseDto;
import com.missionx.questloggers.domain.tip.dto.GetAllTipPostResponseDto;
import com.missionx.questloggers.domain.tip.dto.GetTipPostResponseDto;
import com.missionx.questloggers.domain.tip.service.TipPostService;
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
public class TipPostController {

    private final TipPostService tipPostService;

    @PostMapping("/tips")
    public ResponseEntity<ApiResponse<CreateTipPostResponseDto>> createTipPost(
            @RequestBody CreateTipPostRequestDto createTipPostRequestDto,
            @AuthenticationPrincipal LoginUser loginUser) {

        if (!"ADMIN".equals(loginUser.getRole())) {
            return ApiResponse.error(HttpStatus.FORBIDDEN, "팁 작성은 관리자만 가능합니다.");
        }

        CreateTipPostResponseDto createTipPostResponseDto = tipPostService.createTipPost(createTipPostRequestDto);
        return ApiResponse.success(
                HttpStatus.CREATED,
                "팁 게시글이 생성되었습니다.",
                createTipPostResponseDto
        );
    }

    @GetMapping("/tips/{tipId}")
    public ResponseEntity<ApiResponse<GetTipPostResponseDto>> getTip(
            @PathVariable Long tipId
    ) {
        GetTipPostResponseDto getTipPostResponseDto = tipPostService.getTipPost(tipId);
        return ApiResponse.success(
                HttpStatus.OK,
                "팁 게시글 조회에 성공했습니다.",
                getTipPostResponseDto
        );
    }

    @GetMapping("/tips")
    public ResponseEntity<ApiResponse<List<GetAllTipPostResponseDto>>> getAllTips() {
        List<GetAllTipPostResponseDto> list = tipPostService.getAllTipPosts();
        return ApiResponse.success(
                HttpStatus.OK,
                "모든 팁 게시글 조회에 성공했습니다.",
                list
        );
    }

    @DeleteMapping("/tips/{tipId}")
    public ResponseEntity<ApiResponse<String>> deleteTip(
            @PathVariable Long tipId,
            @AuthenticationPrincipal LoginUser loginUser) {

        if (!"ADMIN".equals(loginUser.getRole())) {
            return ApiResponse.error(HttpStatus.FORBIDDEN, "팁 삭제는 관리자만 가능합니다.");
        }

        tipPostService.deleteTipPost(tipId);
        return ApiResponse.success(HttpStatus.OK,
                "팁 게시글이 삭제되었습니다.",
                null
        );
    }
}
