package ru.s1riys.lab4.config;

import java.util.*;

import org.springframework.http.*;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import ru.s1riys.lab4.domain.dto.ErrorDetailsResponse;
import ru.s1riys.lab4.domain.dto.ValidationErrorResponse;
import ru.s1riys.lab4.exceptions.ApiException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleRequestValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<String> details = new ArrayList<String>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        ValidationErrorResponse error = ValidationErrorResponse.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .title("Validation error")
                .details(details)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetailsResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorDetailsResponse error = ErrorDetailsResponse.builder()
                .error(ex.getStatus().name())
                .title(ex.getTitle())
                .details(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDetailsResponse> handleAnyException(Exception ex, HttpServletRequest request) {
        ErrorDetailsResponse error = ErrorDetailsResponse.builder()
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .title("Internal error")
                .details("Internal error has occured")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
