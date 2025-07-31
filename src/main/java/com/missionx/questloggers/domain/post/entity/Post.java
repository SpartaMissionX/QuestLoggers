package com.missionx.questloggers.domain.post.entity;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.partyapplicant.dto.UpdatePostRequestDto;
import com.missionx.questloggers.domain.post.enums.Difficulty;
import com.missionx.questloggers.domain.post.enums.PartySize;
import com.missionx.questloggers.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "boss_id", nullable = false)
    private Boss boss;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "difficulty")
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "party_size")
    private PartySize partySize;

    @ManyToOne
    @JoinColumn(name = "char_id")
    private Character character;

    public Post (String title, String content, Character character, Boss boss, Difficulty difficulty, PartySize partySize) {
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
