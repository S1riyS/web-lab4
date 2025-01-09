package ru.s1riys.lab4.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    public static final String DOT_CREATE = "DOT_CREATE";
    public static final String DOT_READ = "DOT_READ";
    public static final String DOT_DELETE = "DOT_DELETE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
