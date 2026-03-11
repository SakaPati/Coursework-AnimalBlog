package io.github.fozeton.blog.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostDto {
    @NotEmpty
    private String title;

    @NotBlank
    private String author;

    private MultipartFile img;

    @NotEmpty
    private String content;
}
