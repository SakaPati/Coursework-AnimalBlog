package client.io.github.fozeton.blog.dto;

import lombok.Data;
import java.util.List;

@Data
public class CommentResponse {
    private String message;
    private List<CommentDto> content;
}