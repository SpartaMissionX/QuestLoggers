package com.missionx.questloggers.domain.character.entity;

import com.missionx.questloggers.domain.characterboss.entity.CharacterBoss;
import com.missionx.questloggers.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "characters")
@NoArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "char_boss_id")
    private List<CharacterBoss> characterBoss = new ArrayList<>();

    @Column(name = "ocid", unique = true, nullable = false)
    private String ocid;

    @Column(name = "char_name", nullable = false)
    private String charName;

    @Column(name = "world_name")
    private String worldName;

    @Column(name = "char_class")
    private String charClass;

    @Column(name = "char_level")
    private Integer charLevel;

    ;@Column(name = "char_image", length = 500)
    private String charImage;

    @Column(name = "owner_char")
    @ColumnDefault("false")
    private boolean ownerChar;

    @Column(name = "char_power")
    private long charPower;


    public Character(User user, String ocid, String charName, String worldName, String charClass, int charLevel) {
        this.user = user;
        this.ocid = ocid;
        this.charName = charName;
        this.worldName = worldName;
        this.charClass = charClass;
        this.charLevel = charLevel;
    }

    public void updateOwnerChar(boolean ownerChar) {
        this.ownerChar = ownerChar;
    }
    public void updateCharImage(String charImage) {
        this.charImage = charImage;
    }
    public void updateCharPower(String statValue) {
        this.charPower = Long.parseLong(statValue);
    }
}
