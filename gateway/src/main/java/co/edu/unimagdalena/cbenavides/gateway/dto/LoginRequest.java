package co.edu.unimagdalena.cbenavides.gateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Email(message = "Please provide a valid username")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
