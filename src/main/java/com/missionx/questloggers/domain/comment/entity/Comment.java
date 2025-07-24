package com.missionx.questloggers.domain.comment.entity;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "char_id")
    private Character character;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String content, Character character, Post post) {
        this.content = content;
        this.character = character;
        this.post = post;
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
