package ru.s1riys.lab4.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException(String title, String message) {
        super(title, message, HttpStatus.BAD_REQUEST);
    }
}
