package com.nhpdev.backendservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @Email(message = "Must be email form")
        @NotBlank(message = "Email should no blank or empty")
        String email,
        @NotBlank(message = "Username should no blank or empty")
        String username,
        @NotBlank(message = "password should no blank or empty")
        @Size(min = 5, message = "password should have at least 5 character")
        String password
) {
}
