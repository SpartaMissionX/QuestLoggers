package com.missionx.questloggers.domain.notification.repository;

import com.missionx.questloggers.domain.notification.entity.Notification;
import com.missionx.questloggers.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverAndIsReadFalseOrderByCreatedAtAsc(User receiver);
    boolean existsByReceiverAndIsReadFalse(User receiver);
}
