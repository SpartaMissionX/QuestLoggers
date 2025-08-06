package com.missionx.questloggers.domain.notification.service;

import com.missionx.questloggers.domain.notification.entity.Notification;
import com.missionx.questloggers.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSupportService {

    private final NotificationRepository notificationRepository;

    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

}
