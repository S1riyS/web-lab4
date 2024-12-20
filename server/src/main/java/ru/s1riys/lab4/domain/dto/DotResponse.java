package ru.s1riys.lab4.domain.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DotResponse {
    private Long id;
    private Float x;
    private Float y;
    private Float r;
    private boolean hit;
    private Instant createdAt;
}
