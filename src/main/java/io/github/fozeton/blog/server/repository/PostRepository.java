package io.github.fozeton.blog.server.repository;

import io.github.fozeton.blog.server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
