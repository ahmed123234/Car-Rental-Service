package com.example.carrentalservice.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        // Create payload containing exception details
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
       ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        // Return response entity
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
