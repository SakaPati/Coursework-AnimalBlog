package io.github.fozeton.blog.dto;

import lombok.Getter;

@Getter
public class ErrorMessage {
    private final String error;

    public ErrorMessage(String error) {
        this.error = error;
    }
}
