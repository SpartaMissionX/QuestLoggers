package com.missionx.questloggers.domain.post.repository;

import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL")
    Page<Post> findByDeletedAtNull(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.boss.id = :bossId AND p.deletedAt IS NULL")
    Page<Post> findByBossIdAndDeletedAtNull(Long bossId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.difficulty = :difficulty AND p.deletedAt IS NULL")
    Page<Post> findByDifficultyAndDeletedAtNull(Difficulty difficulty, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.boss.id = :bossId AND p.difficulty = :difficulty AND p.deletedAt IS NULL")
    Page<Post> findByBossIdAndDifficultyAndDeletedAtNull(Long bossId, Difficulty difficulty, Pageable pageable);
}
