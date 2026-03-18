package client.io.github.fozeton.blog.dto;

import lombok.Value;

@Value
public class CommentDto {
    long id;
    String avatar;
    String userName;
    String content;
    String createAt;
}
