package com.codigojava.myapp.application.service.command;

import java.util.Objects;

public record RegisterUserCommand(String email, String rawPassword) {
    public RegisterUserCommand {
        Objects.requireNonNull(email, "email is required");
        Objects.requireNonNull(rawPassword, "password is required");
    }
}
