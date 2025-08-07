package com.missionx.questloggers.domain.notification.service;

import com.missionx.questloggers.domain.notification.entity.Notification;
import com.missionx.questloggers.domain.notification.repository.NotificationRepository;
import com.missionx.questloggers.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationSupportService {

    private final NotificationRepository notificationRepository;

    public List<Notification> findAllByReceiverAndIsReadFalseOrderByCreatedAtAsc(User receiver) {
        return notificationRepository.findAllByReceiverAndIsReadFalseOrderByCreatedAtAsc(receiver);
    }

    public boolean existsByReceiverAndIsReadFalse(User receiver) {
        return notificationRepository.existsByReceiverAndIsReadFalse(receiver);
    }

    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

}
