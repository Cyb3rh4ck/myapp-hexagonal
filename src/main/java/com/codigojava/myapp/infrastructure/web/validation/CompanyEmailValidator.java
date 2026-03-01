package com.codigojava.myapp.infrastructure.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyEmailValidator implements ConstraintValidator<CompanyEmail, String> {
    private String domain;

    @Override
    public void initialize(CompanyEmail constraintAnnotation) {
        this.domain = constraintAnnotation.domain();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Deja que @NotBlank/@NotNull manejen nulos.
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        return trimmed.toLowerCase().endsWith(domain.toLowerCase());
    }
}
