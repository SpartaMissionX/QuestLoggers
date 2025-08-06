package com.missionx.questloggers.domain.notification.service;

import com.missionx.questloggers.domain.notification.entity.Notification;
import com.missionx.questloggers.domain.notification.repository.NotificationRepository;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSupportService notificationSupportService;
    private final SseEmiterService sseEmiterService;


}
