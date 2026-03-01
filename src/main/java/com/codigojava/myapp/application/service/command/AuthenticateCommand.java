package com.codigojava.myapp.application.service.command;

import java.util.Objects;

public record AuthenticateCommand(String email, String rawPassword) {
    public AuthenticateCommand {
        Objects.requireNonNull(email, "email is required");
        Objects.requireNonNull(rawPassword, "password is required");
    }
}
