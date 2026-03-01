package com.codigojava.myapp.infrastructure.config;

import com.codigojava.myapp.application.port.in.AuthenticateUserUseCase;
import com.codigojava.myapp.application.port.in.GetUserUseCase;
import com.codigojava.myapp.application.port.in.RegisterUserUseCase;
import com.codigojava.myapp.application.port.out.PasswordHasher;
import com.codigojava.myapp.application.port.out.UserRepository;
import com.codigojava.myapp.application.service.UserService;
import com.codigojava.myapp.infrastructure.security.BCryptPasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordHasher passwordHasher, Clock clock) {
        return new UserService(userRepository, passwordHasher, clock);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserService userService) {
        return userService;
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserService userService) {
        return userService;
    }

    @Bean
    public GetUserUseCase getUserUseCase(UserService userService) {
        return userService;
    }
}
