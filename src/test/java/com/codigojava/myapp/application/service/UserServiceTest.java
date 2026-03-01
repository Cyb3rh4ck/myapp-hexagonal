package com.codigojava.myapp.application.service;

import com.codigojava.myapp.application.exception.InvalidCredentialsException;
import com.codigojava.myapp.application.exception.UserAlreadyExistsException;
import com.codigojava.myapp.application.exception.UserNotFoundException;
import com.codigojava.myapp.application.port.out.PasswordHasher;
import com.codigojava.myapp.application.port.out.UserRepository;
import com.codigojava.myapp.application.service.command.AuthenticateCommand;
import com.codigojava.myapp.application.service.command.RegisterUserCommand;
import com.codigojava.myapp.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private final Clock clock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC);
    private InMemoryUserRepository repository;
    private PasswordHasher passwordHasher;
    private UserService userService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        passwordHasher = new PlainPasswordHasher();
        userService = new UserService(repository, passwordHasher, clock);
    }

    @Test
    void registers_user_with_hashed_password() {
        var result = userService.register(new RegisterUserCommand("john@example.com", "password123"));

        assertThat(result.email()).isEqualTo("john@example.com");
        User saved = repository.findById(result.id()).orElseThrow();
        assertThat(passwordHasher.matches("password123", saved.passwordHash())).isTrue();
        assertThat(saved.createdAt()).isEqualTo(Instant.parse("2024-01-01T00:00:00Z"));
    }

    @Test
    void fails_when_email_exists() {
        userService.register(new RegisterUserCommand("john@example.com", "password123"));

        assertThatThrownBy(() -> userService.register(new RegisterUserCommand("john@example.com", "anotherPass")))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void authenticates_existing_user() {
        var registerResult = userService.register(new RegisterUserCommand("john@example.com", "password123"));

        var authResult = userService.authenticate(new AuthenticateCommand("john@example.com", "password123"));

        assertThat(authResult.id()).isEqualTo(registerResult.id());
        assertThat(authResult.email()).isEqualTo("john@example.com");
        assertThat(authResult.authenticatedAt()).isEqualTo(Instant.parse("2024-01-01T00:00:00Z"));
    }

    @Test
    void rejects_invalid_credentials() {
        userService.register(new RegisterUserCommand("john@example.com", "password123"));

        assertThatThrownBy(() -> userService.authenticate(new AuthenticateCommand("john@example.com", "wrong")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void finds_user_by_id() {
        var registerResult = userService.register(new RegisterUserCommand("john@example.com", "password123"));

        var result = userService.getById(registerResult.id());

        assertThat(result.email()).isEqualTo("john@example.com");
    }

    @Test
    void fails_when_user_missing() {
        UUID missingId = UUID.randomUUID();
        assertThatThrownBy(() -> userService.getById(missingId))
                .isInstanceOf(UserNotFoundException.class);
    }

    private static class InMemoryUserRepository implements UserRepository {
        private final Map<UUID, User> storage = new HashMap<>();

        @Override
        public Optional<User> findByEmail(String email) {
            return storage.values().stream()
                    .filter(user -> user.email().equals(email))
                    .findFirst();
        }

        @Override
        public Optional<User> findById(UUID id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public User save(User user) {
            storage.put(user.id(), user);
            return user;
        }
    }

    private static class PlainPasswordHasher implements PasswordHasher {
        @Override
        public String hash(String rawPassword) {
            return "hashed-" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String hashedPassword) {
            return hashedPassword.equals(hash(rawPassword));
        }
    }
}
