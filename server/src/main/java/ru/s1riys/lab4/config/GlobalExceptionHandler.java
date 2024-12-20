package ru.s1riys.lab4.config;

import java.util.*;

import org.springframework.http.*;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleRequestValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();

        List<String> details = new ArrayList<String>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("errors", details);
        responseBody.put("path", request.getServletPath());

        return responseBody;
    }

}
