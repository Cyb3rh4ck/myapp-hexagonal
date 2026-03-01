package com.codigojava.myapp.infrastructure.persistence.jpa;

import com.codigojava.myapp.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaUserRepositoryAdapterTest {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    private JpaUserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JpaUserRepositoryAdapter(springDataUserRepository);
    }

    @Test
    void saves_and_finds_user() {
        User user = new User(UUID.randomUUID(), "jane@example.com", "hashed-pass", Set.of("USER"), Instant.parse("2024-01-02T00:00:00Z"));

        adapter.save(user);

        Optional<User> found = adapter.findByEmail("jane@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().email()).isEqualTo("jane@example.com");
    }
}
