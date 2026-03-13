package io.github.fozeton.blog.dto;

import lombok.Value;

import java.util.List;

@Value
public class PostDto {
    int id;
    String title;
    String author;
    String imageUrl;
    String content;
    int likes;
    List<CommentDto> comments;
    String createAt;
}
