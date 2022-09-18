package com.example.carrentalservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ApiRequestExceptionTest {
    /**
     * Method under test: {@link ApiRequestException#ApiRequestException(String)}
     */
    @Test
    void testConstructor() {
        ApiRequestException actualApiRequestException = new ApiRequestException("An error occurred");
        assertNull(actualApiRequestException.getCause());
        assertEquals(0, actualApiRequestException.getSuppressed().length);
        assertEquals("An error occurred", actualApiRequestException.getMessage());
        assertEquals("An error occurred", actualApiRequestException.getLocalizedMessage());
        assertNull(actualApiRequestException.getHttpStatus());
    }

    /**
     * Method under test: {@link ApiRequestException#ApiRequestException(String, HttpStatus)}
     */
    @Test
    void testConstructor2() {
        ApiRequestException actualApiRequestException = new ApiRequestException("An error occurred", HttpStatus.CONTINUE);

        assertNull(actualApiRequestException.getCause());
        assertEquals(0, actualApiRequestException.getSuppressed().length);
        assertEquals("An error occurred", actualApiRequestException.getMessage());
        assertEquals("An error occurred", actualApiRequestException.getLocalizedMessage());
        assertEquals(HttpStatus.CONTINUE, actualApiRequestException.getHttpStatus());
    }
}

