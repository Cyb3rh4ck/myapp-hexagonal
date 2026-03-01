package com.codigojava.myapp.infrastructure.persistence.jpa;

import com.codigojava.myapp.application.port.out.UserRepository;
import com.codigojava.myapp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository repository;

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity saved = repository.save(UserEntityMapper.toEntity(user));
        return UserEntityMapper.toDomain(saved);
    }
}
