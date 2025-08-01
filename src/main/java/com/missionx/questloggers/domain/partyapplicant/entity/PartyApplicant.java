package com.missionx.questloggers.domain.partyapplicant.entity;

import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.partyapplicant.enums.ApplicantStatus;
import com.missionx.questloggers.domain.character.entity.Character;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PartyApplicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "char_id", nullable = false)
    private Character character;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicantStatus status;

    public PartyApplicant(Post post, Character character) {
        this.post = post;
        this.character = character;
        this.status = ApplicantStatus.PENDING;
    }

    public void acceptStatus() {
        this.status = ApplicantStatus.ACCEPTED;
    }

    public void rejectStatus() {
        this.status = ApplicantStatus.REJECTED;
    }

    public void pendingStatus() {
        this.status = ApplicantStatus.PENDING;
    }

    public void leaveStatus() {
        this.status = ApplicantStatus.LEAVE;
    }
}