package com.codigojava.myapp.application.service.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AuthResult(UUID id, String email, Set<String> roles, Instant authenticatedAt, Instant createdAt) {
}
