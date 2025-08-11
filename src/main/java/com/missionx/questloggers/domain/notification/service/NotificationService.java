package com.missionx.questloggers.domain.notification.service;

import com.missionx.questloggers.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSupportService notificationSupportService;
    private final SseEmitterService sseEmitterService;


}
