package io.github.fozeton.blog.dto;

import lombok.Value;

@Value
public class CommentDto {
    String avatar;
    String userName;
    String message;
}
