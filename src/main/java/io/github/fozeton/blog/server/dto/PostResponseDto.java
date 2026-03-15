package io.github.fozeton.blog.server.dto;

import io.github.fozeton.blog.server.entity.Comment;
import io.github.fozeton.blog.server.entity.Like;
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
    List<Like> likes;
    List<Comment> comments;
    LocalDateTime createAt;
    boolean isLiked;
}
