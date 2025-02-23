package ru.s1riys.lab4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.s1riys.lab4.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
