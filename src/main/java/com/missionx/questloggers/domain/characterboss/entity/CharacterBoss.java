package com.missionx.questloggers.domain.characterboss.entity;

import com.missionx.questloggers.domain.boss.entity.Boss;
import com.missionx.questloggers.domain.character.entity.Character;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "character_boss")
@NoArgsConstructor
public class CharacterBoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "char_id")
    private Character character;

    @ManyToOne
    @JoinColumn(name = "boss_id")
    private Boss boss;

    @Column(name = "is_cleared")
    @ColumnDefault("false")
    private boolean isCleared;

    @Column(name = "clear_count")
    @ColumnDefault("0")
    private int clearCount;

    public CharacterBoss(Character character, Boss boss) {
        this.character = character;
        this.boss = boss;
    }

    public void updateIsCleared(boolean isCleared) {
        this.isCleared = isCleared;
        this.clearCount = clearCount + 1;
    }
}
