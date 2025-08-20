package com.missionx.questloggers.domain.partymember.entity;

import com.missionx.questloggers.domain.character.entity.Character;
import com.missionx.questloggers.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "party_members")
public class PartyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "char_id", nullable = false)
    private Character character;

    public PartyMember(Post post, Character character) {
        this.post = post;
        this.character = character;
    }
}
