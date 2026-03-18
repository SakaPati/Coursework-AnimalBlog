package server.io.github.fozeton.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadFileDto {
    @NotBlank(message = "Type cannot be empty or contain spaces")
    private String type;

    @NotBlank
    private String userName;

    private MultipartFile img;
}
