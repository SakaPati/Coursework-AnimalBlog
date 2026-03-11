package io.github.fozeton.blog.server.repository;

import io.github.fozeton.blog.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String user);
    @Nullable
    User findByUserName(String user);
}