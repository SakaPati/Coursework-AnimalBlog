package server.io.github.fozeton.blog.repository;

import server.io.github.fozeton.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Post findPostById(long id);
    List<Post> findByAuthor(String author);
}
