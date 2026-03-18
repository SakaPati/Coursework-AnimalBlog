package server.io.github.fozeton.blog.repository;

import server.io.github.fozeton.blog.entity.Like;
import server.io.github.fozeton.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsLikeByUserAndPost(String user, Post post);

    @Transactional
    void deleteByUserAndPost(String user, Post post);

    int countByPostId(Long postId);
}
