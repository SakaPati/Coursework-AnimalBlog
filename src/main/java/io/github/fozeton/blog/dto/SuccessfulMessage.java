package io.github.fozeton.blog.dto;

import lombok.Getter;

@Getter
public class SuccessfulMessage {
    private final String message;
    public SuccessfulMessage(String message) {
        this.message = message;
    }
}
