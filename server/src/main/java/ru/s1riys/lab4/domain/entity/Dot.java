package ru.s1riys.lab4.domain.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dots")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dot {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "x", nullable = false)
    private Float x;
    @Column(name = "y", nullable = false)
    private Float y;
    @Column(name = "r", nullable = false)
    private Float r;
    @Column(name = "is_hit", nullable = false)
    private boolean hit;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
