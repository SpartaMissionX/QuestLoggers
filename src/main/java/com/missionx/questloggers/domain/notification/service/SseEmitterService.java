package com.missionx.questloggers.domain.notification.service;

import com.missionx.questloggers.domain.notification.entity.Notification;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.service.UserSupportService;
import com.missionx.questloggers.global.config.security.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NotificationSupportService notificationSupportService;
    private final UserSupportService userSupportService;


    @Transactional
    public SseEmitter subscribe(LoginUser loginUser) {
        SseEmitter emitter = new SseEmitter(1000L * 60 * 60); // 60분 타임아웃 설정
        User user = userSupportService.findUserById(loginUser.getUserId());

        emitters.put(user.getId(), emitter);

        // 타임아웃이나 완료 시 맵에서 제거
        emitter.onCompletion(() -> emitters.remove(user.getId()));
        emitter.onTimeout(() -> emitters.remove(user.getId()));

        // 최초 연결 시 더미 데이터를 전송하여 연결을 유지함
        boolean existsByReceiverAndIsReadFalse = notificationSupportService.existsByReceiverAndIsReadFalse(user);
        try {
            emitter.send(SseEmitter.event().name("Connect").data("연결성공"));
            if (existsByReceiverAndIsReadFalse) {
                List<Notification> notificationList = notificationSupportService.findAllByReceiverAndIsReadFalseOrderByCreatedAtAsc(user);
                for (Notification notification : notificationList) {
                    emitter.send(SseEmitter.event().name(notification.getStatus().toString()).data(notification.getMessage()));
                    notification.updateIsRead();
                }
            }
        } catch (Exception e) {
            emitters.remove(user.getId());
        }

        return emitter;
    }

    @Transactional
    public void sendEvent(User user, Notification notification) {
        SseEmitter emitter = emitters.get(user.getId());

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(notification.getStatus().toString()).data(notification.getMessage()));
                notification.updateIsRead();
            } catch (IOException e) {
                notificationSupportService.save(notification);
                emitters.remove(user.getId());
            }
        } else {
            notificationSupportService.save(notification);
        }
    }

}
