package io.github.fozeton.blog.server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "ind_userName", columnList = "userName", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatarUrl;

    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Setter(AccessLevel.NONE)
    @Column(updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();
}