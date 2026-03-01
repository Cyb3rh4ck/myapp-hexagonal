package com.codigojava.myapp.infrastructure.persistence.jpa;

import com.codigojava.myapp.domain.User;

import java.util.HashSet;
import java.util.Set;

public final class UserEntityMapper {

    private UserEntityMapper() {
    }

    public static UserEntity toEntity(User user) {
        Set<String> rolesCopy = new HashSet<>(user.roles());
        return new UserEntity(user.id(), user.email(), user.passwordHash(), user.createdAt(), rolesCopy);
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getEmail(), entity.getPasswordHash(), new HashSet<>(entity.getRoles()), entity.getCreatedAt());
    }
}
