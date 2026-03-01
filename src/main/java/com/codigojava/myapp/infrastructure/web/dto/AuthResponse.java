package com.codigojava.myapp.infrastructure.web.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AuthResponse(UUID id, String email, Set<String> roles, Instant authenticatedAt, Instant createdAt) {
}
