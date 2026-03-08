package io.github.fozeton.blog.server.repository;

import io.github.fozeton.blog.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String user);
}
