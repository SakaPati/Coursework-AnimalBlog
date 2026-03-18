package server.io.github.fozeton.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponseDto {
    long id;
    String title;
    String author;
    String imageUrl;
    String content;
    long likes;
    List<CommentResponseDto> comments;
    LocalDateTime createAt;
    boolean isLiked;
}
