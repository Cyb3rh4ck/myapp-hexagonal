package com.codigojava.myapp.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Domain aggregate for a user.
 */
public record User(UUID id, String email, String passwordHash, Set<String> roles, Instant createdAt) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public User(UUID id, String email, String passwordHash, Set<String> roles, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.email = Objects.requireNonNull(email, "email is required");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash is required");
        this.roles = Set.copyOf(Objects.requireNonNull(roles, "roles is required"));
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("invalid email format");
        }
        if (passwordHash.isBlank()) {
            throw new IllegalArgumentException("password hash cannot be blank");
        }
    }

    public User withPasswordHash(String newHash) {
        return new User(this.id, this.email, newHash, this.roles, this.createdAt);
    }
}
