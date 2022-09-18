package com.example.carrentalservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ApiExceptionHandlerTest {
    /**
     * Method under test: {@link ApiExceptionHandler#handleApiRequestException(ApiRequestException)}
     */
    @Test
    void testHandleApiRequestException() {
        ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();
        ApiRequestException apiRequestException = new ApiRequestException("An error occurred");
        ResponseEntity<Object> actualHandleApiRequestExceptionResult = apiExceptionHandler
                .handleApiRequestException(apiRequestException);
        assertTrue(actualHandleApiRequestExceptionResult.hasBody());
        assertTrue(actualHandleApiRequestExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleApiRequestExceptionResult.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,
                ((ApiException) actualHandleApiRequestExceptionResult.getBody()).getHttpStatus());
        assertEquals("An error occurred", ((ApiException) actualHandleApiRequestExceptionResult.getBody()).getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, apiRequestException.getHttpStatus());
    }

    /**
     * Method under test: {@link ApiExceptionHandler#handleApiRequestException(ApiRequestException)}
     */
    @Test
    void testHandleApiRequestException2() {
        assertThrows(IllegalArgumentException.class, () -> (new ApiExceptionHandler()).handleApiRequestException(null));
    }

    /**
     * Method under test: {@link ApiExceptionHandler#handleApiRequestException(ApiRequestException)}
     */
    @Test
    void testHandleApiRequestException3() {
        ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();
        ResponseEntity<Object> actualHandleApiRequestExceptionResult = apiExceptionHandler
                .handleApiRequestException(new ApiRequestException("An error occurred", HttpStatus.CONTINUE));
        assertTrue(actualHandleApiRequestExceptionResult.hasBody());
        assertTrue(actualHandleApiRequestExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.CONTINUE, actualHandleApiRequestExceptionResult.getStatusCode());
        assertEquals(HttpStatus.CONTINUE,
                ((ApiException) actualHandleApiRequestExceptionResult.getBody()).getHttpStatus());
        assertEquals("An error occurred", ((ApiException) actualHandleApiRequestExceptionResult.getBody()).getMessage());
    }
}

