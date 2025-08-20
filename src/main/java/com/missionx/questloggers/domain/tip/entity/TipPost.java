package com.missionx.questloggers.domain.tip.entity;

import com.missionx.questloggers.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tip_posts_test")
@NoArgsConstructor
public class TipPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    public TipPost(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // 수정기능 만들 때 쓰기
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
