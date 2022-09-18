package com.example.carrentalservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiExceptionTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ApiException#ApiException(String, HttpStatus, ZonedDateTime)}
     *   <li>{@link ApiException#getHttpStatus()}
     *   <li>{@link ApiException#getMessage()}
     *   <li>{@link ApiException#getZonedDateTime()}
     * </ul>
     */
    @Test
    void testConstructor() {
        ApiException actualApiException = new ApiException("An error occurred", HttpStatus.CONTINUE, null);

        assertEquals(HttpStatus.CONTINUE, actualApiException.getHttpStatus());
        assertEquals("An error occurred", actualApiException.getMessage());
        assertNull(actualApiException.getZonedDateTime());
    }
}

