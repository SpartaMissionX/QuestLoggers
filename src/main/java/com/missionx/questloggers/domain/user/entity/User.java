package com.missionx.questloggers.domain.user.entity;

import com.missionx.questloggers.domain.user.enums.Role;
import com.missionx.questloggers.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // pk
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String apiKey;

    @Column(nullable = false)
    private int point = 0;

    private Long ownerCharId;

    private String ownerCharName;

    //soft delete 추가
    @Column(nullable = false)
    private boolean isDeleted = false;

    public User(String email, String password, String apiKey, Role role) {
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
        this.role = role;
        this.point = 0;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateOwnerChar(Long ownerCharId, String ownerCharName) {
        this.ownerCharId = ownerCharId;
        this.ownerCharName = ownerCharName;
    }
}
