package io.github.fozeton.blog.dto;

import lombok.Data;
import java.util.List;

@Data
public class PostDto {
    int id;
    String title;
    String author;
    String imageUrl;
    String content;
    List<LikeDto> likes;
    List<CommentDto> comments;
    String createAt;
    boolean liked;
}
