package ru.s1riys.lab4.domain.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailsResponse {
    @Builder.Default
    private Date timestamp = new Date();
    private String error;
    private String title;
    private String details;
}
