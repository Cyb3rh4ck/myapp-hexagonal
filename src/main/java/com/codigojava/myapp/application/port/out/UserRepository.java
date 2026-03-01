package com.codigojava.myapp.application.port.out;

import com.codigojava.myapp.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    User save(User user);
}
