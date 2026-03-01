package com.codigojava.myapp.infrastructure.web;

import com.codigojava.myapp.application.port.in.AuthenticateUserUseCase;
import com.codigojava.myapp.application.port.in.GetUserUseCase;
import com.codigojava.myapp.application.port.in.RegisterUserUseCase;
import com.codigojava.myapp.application.service.command.AuthenticateCommand;
import com.codigojava.myapp.application.service.command.RegisterUserCommand;
import com.codigojava.myapp.application.service.model.AuthResult;
import com.codigojava.myapp.application.service.model.UserResult;
import com.codigojava.myapp.infrastructure.web.dto.AuthRequest;
import com.codigojava.myapp.infrastructure.web.dto.AuthResponse;
import com.codigojava.myapp.infrastructure.web.dto.RegisterUserRequest;
import com.codigojava.myapp.infrastructure.web.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final GetUserUseCase getUserUseCase;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResult result = registerUserUseCase.register(new RegisterUserCommand(request.email(), request.password()));
        UserResponse response = new UserResponse(result.id(), result.email(), result.roles(), result.createdAt());
        URI location = URI.create("/api/users/" + result.id());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/auth/login")
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
        AuthResult result = authenticateUserUseCase.authenticate(new AuthenticateCommand(request.email(), request.password()));
        return new AuthResponse(result.id(), result.email(), result.roles(), result.authenticatedAt(), result.createdAt());
    }

    @GetMapping("/users/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        UserResult result = getUserUseCase.getById(id);
        return new UserResponse(result.id(), result.email(), result.roles(), result.createdAt());
    }
}
