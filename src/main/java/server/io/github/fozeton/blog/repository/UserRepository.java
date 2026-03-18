package server.io.github.fozeton.blog.repository;

import server.io.github.fozeton.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String user);

    @Nullable
    User findByUserName(String user);

    @Query("SELECT u.avatarUrl from User u WHERE u.userName = :userName")
    String findUserAvatarUrlByUserName(@Param("userName") String userName);
}