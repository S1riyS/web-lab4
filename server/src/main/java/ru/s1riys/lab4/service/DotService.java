package ru.s1riys.lab4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.s1riys.lab4.domain.dto.CreateDotRequest;
import ru.s1riys.lab4.domain.entity.Dot;
import ru.s1riys.lab4.domain.entity.User;
import ru.s1riys.lab4.repository.DotRepository;

@Service
public class DotService {
    @Autowired
    private DotRepository dotRepository;

    public Dot createDot(CreateDotRequest dto, User owner) {
        boolean isHit = this.checkIfHit(dto.getX(), dto.getY(), dto.getR());
        Dot dot = Dot.builder()
                .x(dto.getX())
                .y(dto.getY())
                .r(dto.getR())
                .hit(isHit)
                .owner(owner)
                .build();
        return dotRepository.save(dot);
    }

    public void deleteAllByOwner(User owner) {
        dotRepository.deleteAllByUserId(owner.getId());
    }

    private boolean checkIfHit(float x, float y, float r) {
        boolean triangle = (x <= 0 && y >= 0) && (y <= x + r);
        boolean rect = (x >= 0 && x <= r) && (y >= 0 && y <= r / 2);
        boolean circle = (x >= 0 && y <= 0) && (x * x + y * y <= r * r);

        return triangle || rect || circle;
    }
}
