package server.io.github.fozeton.blog.exceptions;

public class PostCreationException extends RuntimeException {
    public PostCreationException(String message) {
        super(message);
    }
}
