package com.missionx.questloggers.domain.test.party;

import com.missionx.questloggers.domain.post.dto.ApplyPartyResponseDto;
import com.missionx.questloggers.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestApplyPartyController {

    private final TestApplyPartyService testApplyPartyService;

    @PostMapping("test/posts/applicants")
    public ResponseEntity<ApiResponse<ApplyPartyResponseDto>> testApplyToParty(Long userId) {
        ApplyPartyResponseDto responseDto = testApplyPartyService.applyPartyResponseDto(userId);
        return ApiResponse.success(HttpStatus.CREATED, "파티 신청이 완료되었습니다.", responseDto);
    }
}
