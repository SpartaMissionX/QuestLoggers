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

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String apiKey;

    private int point;

    //soft delete 추가
    @Column(nullable = false)
    private boolean isDeleted = false;

    public User(String email, String password, String apiKey) {
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
