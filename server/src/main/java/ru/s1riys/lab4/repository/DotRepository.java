package ru.s1riys.lab4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import ru.s1riys.lab4.domain.entity.Dot;

public interface DotRepository extends JpaRepository<Dot, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Dot d WHERE d.owner.id = ?1")
    void deleteAllByUserId(Long userId);
}
