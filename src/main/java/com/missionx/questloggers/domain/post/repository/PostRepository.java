package com.missionx.questloggers.domain.post.repository;

import com.missionx.questloggers.domain.post.entity.Post;
import com.missionx.questloggers.domain.post.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시물 페이징 및 키워드 검색에서 사용
    Page<Post> findByTitleContainingAndDeletedAtNull(String title,Pageable pageable);
    Page<Post> findByDeletedAtNull(Pageable pageable);
    Optional<Post> findByIdAndDeletedAtNull(Long postId);
    Page<Post> findByBossIdAndDeletedAtNull(Long bossId, Pageable pageable);
    Page<Post> findByDifficultyAndDeletedAtNull(Difficulty difficulty, Pageable pageable);
    Page<Post> findByBossIdAndDifficultyAndDeletedAtNull(Long bossId, Difficulty difficulty, Pageable pageable);
}
