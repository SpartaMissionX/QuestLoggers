package com.missionx.questloggers.domain.notification.controller;

import com.missionx.questloggers.domain.notification.service.SseEmiterService;
import com.missionx.questloggers.global.config.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SseEmitterController {
    private final SseEmiterService sseEmiterService;

    @GetMapping(value = "/posts/{postId}/applicant/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @PathVariable Long postId,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        return sseEmiterService.subscribe(postId, loginUser);
    }
}
