package com.missionx.questloggers.domain.notification.entity;

import com.missionx.questloggers.domain.notification.enums.NotificationStatus;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 전송 대상자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User receiver;

    // 메세지
    private String message;

    // 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private NotificationStatus status;

    // 조회 여부
    private boolean isRead;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Notification(User receiver, String message, Post post, NotificationStatus status) {
        this.receiver = receiver;
        this.message = message;
        this.post = post;
        this.status = status;
        this.isRead = false;
    }

    public void updateIsRead() {
        this.isRead = true;
    }
}
