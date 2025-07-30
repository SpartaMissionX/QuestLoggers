package com.missionx.questloggers.domain.post.entity;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.post.dto.UpdatePostRequestDto;
import com.missionx.questloggers.domain.post.enums.Difficulty;
import com.missionx.questloggers.domain.post.enums.PartySize;
import com.missionx.questloggers.domain.user.entity.User;
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

    @Column(nullable = false, name = "boss_id")
    private Long bossId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "difficulty")
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "party_size")
    private PartySize partySize;

    @ManyToOne
    @JoinColumn(name = "char_id")
    private Character character;

    public Post (String title, String content, Character character, Long bossId, Difficulty difficulty, PartySize partySize) {
        this.title = title;
        this.content = content;
        this.bossId = bossId;
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
