package th.ac.ku.restaurant.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, message = "Username is mandatory and at least 4 characters in length")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password is mandatory and at least 8 characters in length")
    private String password;
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name can only contain letters")
    private String name;
}