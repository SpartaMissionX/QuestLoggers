package com.missionx.questloggers.domain.boss.entity;

import com.missionx.questloggers.domain.boss.dto.CreateBossRequestDto;
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

    @Column(name = "boss_name", nullable = false, length = 50)
    private String bossName;

    @Column(name = "boss_image", nullable = false , columnDefinition = "TEXT")
    private String bossImage;

    //보스 생성 생성자
    public Boss(CreateBossRequestDto createBossRequestDto) {
        this.bossName = createBossRequestDto.getBossName();
        this.bossImage = createBossRequestDto.getBossImage();
    }
}
