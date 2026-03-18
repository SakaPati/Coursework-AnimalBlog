package client.io.github.fozeton.blog.dto;

import lombok.Value;

import java.util.List;

@Value
public class PostsDto {
    String message;
    List<PostDto> content;
}
