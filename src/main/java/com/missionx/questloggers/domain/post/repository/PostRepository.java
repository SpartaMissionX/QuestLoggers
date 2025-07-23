package com.missionx.questloggers.domain.post.repository;

import com.missionx.questloggers.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시물 페이징 및 키워드 검색에서 사용
    Page<Post> findByTitleContainingAndDeletedAtNull(String title,Pageable pageable);
    Page<Post> findByDeletedAtNull(Pageable pageable);

}
