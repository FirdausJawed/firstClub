package com.org.firstclub.repository.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank(message = "Name is required") // Rejects null and "" and " "
    private String name;

    @Email(message = "Invalid email format") // Checks for @ and . structure
    @NotBlank(message = "Email is required")
    private String email;
}
