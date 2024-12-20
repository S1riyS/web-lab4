package ru.s1riys.lab4.domain.mapper;

import org.springframework.stereotype.Component;

import ru.s1riys.lab4.domain.dto.DotResponse;
import ru.s1riys.lab4.domain.entity.Dot;

@Component
public class DotMapper {
    public DotResponse toResponse(Dot dot) {
        return DotResponse.builder()
                .id(dot.getId())
                .x(dot.getX())
                .y(dot.getY())
                .r(dot.getR())
                .hit(dot.isHit())
                .createdAt(dot.getCreatedAt())
                .build();
    }
}
