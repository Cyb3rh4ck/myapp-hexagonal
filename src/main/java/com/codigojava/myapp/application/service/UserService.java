package com.codigojava.myapp.application.service;

import com.codigojava.myapp.application.exception.InvalidCredentialsException;
import com.codigojava.myapp.application.exception.UserAlreadyExistsException;
import com.codigojava.myapp.application.exception.UserNotFoundException;
import com.codigojava.myapp.application.port.in.AuthenticateUserUseCase;
import com.codigojava.myapp.application.port.in.GetUserUseCase;
import com.codigojava.myapp.application.port.in.RegisterUserUseCase;
import com.codigojava.myapp.application.port.out.PasswordHasher;
import com.codigojava.myapp.application.port.out.UserRepository;
import com.codigojava.myapp.application.service.command.AuthenticateCommand;
import com.codigojava.myapp.application.service.command.RegisterUserCommand;
import com.codigojava.myapp.application.service.model.AuthResult;
import com.codigojava.myapp.application.service.model.UserResult;
import com.codigojava.myapp.domain.User;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, AuthenticateUserUseCase, GetUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final Clock clock;

    @Override
    public UserResult register(RegisterUserCommand command) {
        userRepository.findByEmail(command.email())
                .ifPresent(existing -> {
                    throw new UserAlreadyExistsException(command.email());
                });

        String hashed = passwordHasher.hash(command.rawPassword());
        Instant now = Instant.now(clock);
        User user = new User(UUID.randomUUID(), command.email(), hashed, Set.of("USER"), now);
        User saved = userRepository.save(user);
        return toResult(saved);
    }

    @Override
    public AuthResult authenticate(AuthenticateCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(command.rawPassword(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        Instant authenticatedAt = Instant.now(clock);
        return new AuthResult(user.id(), user.email(), user.roles(), authenticatedAt, user.createdAt());
    }

    @Override
    public UserResult getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return toResult(user);
    }

    private UserResult toResult(User user) {
        return new UserResult(user.id(), user.email(), user.roles(), user.createdAt());
    }
}
