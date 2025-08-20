package com.missionx.questloggers.domain.post.entity;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.partyapplicant.dto.UpdatePostRequestDto;
import com.missionx.questloggers.domain.post.enums.Difficulty;
import com.missionx.questloggers.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "posts",
        indexes = {
                @Index(name = "idx_boss_difficulty_created", columnList = "boss_id, difficulty, created_at"),
                @Index(name = "idx_difficulty_created", columnList = "difficulty, created_at"),
                @Index(name = "idx_created_at", columnList = "created_at")
        }
)
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "char_id")
    private Character character;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "boss_id", nullable = false)
    private Boss boss;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Column(name = "party_size", nullable = false)
    private int partySize;

    public Post (String title, String content, Character character, Boss boss, Difficulty difficulty, int partySize) {
        this.title = title;
        this.content = content;
        this.boss = boss;
        this.character = character;
        this.difficulty = difficulty;
        this.partySize = partySize;
    }

    public void updatePost(UpdatePostRequestDto updatePostRequestDto) {
        this.title = updatePostRequestDto.getTitle();
        this.content = updatePostRequestDto.getContent();
        this.partySize = updatePostRequestDto.getPartySize();
    }
}
