package com.codigojava.myapp.infrastructure.web.dto;

import com.codigojava.myapp.infrastructure.web.validation.CompanyEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank @Email @CompanyEmail String email,
        @NotBlank @Size(min = 8, max = 72) String password
) {
}
