package ru.s1riys.lab4.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDotRequest {
    @NotNull
    @Min(value = -3, message = "X должен быть больше -3")
    @Max(value = 5, message = "X должен быть меньше 5")
    public Float x;

    @NotNull
    @Min(value = -5, message = "Y должен быть больше -5")
    @Max(value = 3, message = "Y должен быть меньше 3")
    public Float y;

    @NotNull
    @Min(value = 0, message = "R должен быть больше 0")
    @Max(value = 5, message = "R должен быть меньше 5")
    public Float r;
}
