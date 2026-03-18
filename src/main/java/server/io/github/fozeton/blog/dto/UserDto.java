package server.io.github.fozeton.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    @NotBlank(message = "The name must not be empty or contain spaces")
    @Size(min = 3, max = 24, message = "Login must be between 3 and 24 characters long")
    private String userName;

    @NotBlank(message = "The password cannot be empty or contain spaces")
    @Size(min = 8, message = "The password must be 8 or more characters long")
    private String password;
}
