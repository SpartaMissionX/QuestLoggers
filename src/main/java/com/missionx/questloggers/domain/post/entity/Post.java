package com.missionx.questloggers.domain.post.entity;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.post.dto.UpdatePostRequestDto;
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

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "char_id")
    private Character character;

    public Post (String title, String content, Character character) {
        this.title = title;
        this.content = content;
        this.character = character;
    }

    public void updatePost(UpdatePostRequestDto updatePostRequestDto) {
        this.title = updatePostRequestDto.getTitle();
        this.content = updatePostRequestDto.getContent();
    }

}
