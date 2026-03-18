package server.io.github.fozeton.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    long id;
    String avatarUrl;
    String userName;
    String content;
    LocalDateTime createAt;
}
