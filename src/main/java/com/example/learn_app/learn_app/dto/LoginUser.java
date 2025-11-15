package com.example.learn_app.learn_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUser {
    @NotBlank(message = "Username or email is required")

    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;

}
