package com.missionx.questloggers.domain.test.character;

import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestCharController {
    private final TestCharacterService testCharacterService;

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<Object>> createDummyChar(@AuthenticationPrincipal LoginUser loginUser) {
        if (!loginUser.getRole().equals("ADMIN")) {
            return ApiResponse.error(HttpStatus.FORBIDDEN, "테스트 계정 생성은 관리자만 가능합니다.");
        }
        testCharacterService.createUserAndChar();
        return ApiResponse.success(HttpStatus.CREATED, "테스트 계정 생성이 완료되었습니다.", null);
    }

    @PostMapping("/test/posts")
    public ResponseEntity<ApiResponse<Object>> createDummyPost(@AuthenticationPrincipal LoginUser loginUser) {
        if (!loginUser.getRole().equals("ADMIN")) {
            return ApiResponse.error(HttpStatus.FORBIDDEN, "테스트 파티 모집글 작성은 관리자만 가능합니다.");
        }
        testCharacterService.createPost();
        return ApiResponse.success(HttpStatus.CREATED, "테스트 파티 모집글 작성이 완료되었습니다.", null);
    }
}
