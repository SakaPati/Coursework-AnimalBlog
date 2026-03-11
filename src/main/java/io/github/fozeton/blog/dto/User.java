package io.github.fozeton.blog.dto;

import lombok.Getter;

@Getter
public class User {
    private final String userName;
    private final String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
