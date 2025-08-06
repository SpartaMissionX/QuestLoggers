package com.missionx.questloggers.domain.notification.service;

import com.missionx.questloggers.domain.post.dto.PartyApplicantResponseDto;
import com.missionx.questloggers.global.dto.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmiterService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long postId) {
        SseEmitter emitter = new SseEmitter(1000L * 60 * 60); // 60분 타임아웃 설정

        emitters.put(postId, emitter);

        // 타임아웃이나 완료 시 맵에서 제거
        emitter.onCompletion(() -> emitters.remove(postId));
        emitter.onTimeout(() -> emitters.remove(postId));

        // 최초 연결 시 더미 데이터를 전송하여 연결을 유지함
        try {
            emitter.send(SseEmitter.event().name("연결성공").data("연결성공"));
        } catch (Exception e) {
            emitters.remove(postId);
        }

        return emitter;
    }

    public void sendPartyApplicantUpdate(Long postId, PartyApplicantResponseDto updateDate) {
        SseEmitter emitter = emitters.get(postId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("party-applicant-update").data(updateDate));
            } catch (IOException e){
                emitters.remove(postId);
            }
        }
    }
}
