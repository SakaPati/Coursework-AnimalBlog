package io.github.fozeton.blog.server.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class SuccessfulMessage {
    private final String message;
    private List<?> content;
    public SuccessfulMessage(String message) {
        this.message = message;
    }

    public SuccessfulMessage(String message, List<?> content) {
        this.message = message;
        this.content = content;
    }
}
