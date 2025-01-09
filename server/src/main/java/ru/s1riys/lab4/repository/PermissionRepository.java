package ru.s1riys.lab4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.s1riys.lab4.domain.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
