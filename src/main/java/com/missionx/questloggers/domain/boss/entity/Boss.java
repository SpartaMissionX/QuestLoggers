package com.missionx.questloggers.domain.boss.entity;

import com.missionx.questloggers.domain.characterboss.entity.CharacterBoss;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "bosses")
@NoArgsConstructor
public class Boss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "char_boss_id")
    private List<CharacterBoss> characterBoss = new ArrayList<>();

    private String bossName;

    private String bossImage;
}
