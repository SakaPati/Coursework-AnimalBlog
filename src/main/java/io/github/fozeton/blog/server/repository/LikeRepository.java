package io.github.fozeton.blog.server.repository;

import io.github.fozeton.blog.server.entity.Like;
import io.github.fozeton.blog.server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsLikeByUserAndPost(String user, Post post);

    @Transactional
    void deleteByUserAndPost(String user, Post post);

    int countByPostId(Long postId);
}
