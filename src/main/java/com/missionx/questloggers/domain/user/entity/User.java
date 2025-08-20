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
    // 회원탈퇴기능 추가
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @Column(name = "point", nullable = false)
    private int point;

    @Column(name = "owner_char_id")
    private Long ownerCharId;

    @Column(name = "owner_char_name", length = 20)
    private String ownerCharName;

    public User(String email, String password, String apiKey, Role role) {
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
        this.role = role;
        this.point = 0;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateOwnerChar(Long ownerCharId, String ownerCharName) {
        this.ownerCharId = ownerCharId;
        this.ownerCharName = ownerCharName;
    }
}
