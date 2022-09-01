package com.example.carrentalservice.exception;

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

        if (e.getHttpStatus() == null)
            e.setHttpStatus(HttpStatus.BAD_REQUEST);

        ApiException apiException = new ApiException(
                e.getMessage(),
                e.getHttpStatus(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        // Return response entity
        return new ResponseEntity<>(apiException, e.getHttpStatus());
    }
}
