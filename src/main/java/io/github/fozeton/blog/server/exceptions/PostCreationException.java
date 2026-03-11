package io.github.fozeton.blog.server.exceptions;

public class PostCreationException extends RuntimeException {
    public PostCreationException(String message) {
        super(message);
    }
}
