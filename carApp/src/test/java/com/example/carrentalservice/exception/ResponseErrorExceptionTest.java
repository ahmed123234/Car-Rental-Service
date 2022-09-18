package com.example.carrentalservice.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

class ResponseErrorExceptionTest {

    /**
     * Method under test: {@link ResponseErrorException#ResponseError(HttpServletResponse, String, HttpStatus, Exception)}
     */
    @Test
    void testResponseError2() throws IOException {
        assertThrows(IllegalArgumentException.class,
                () -> (new ResponseErrorException()).ResponseError(null, "foo", null, null));
    }

    /**
     * Method under test: {@link ResponseErrorException#ResponseError(HttpServletResponse, String, HttpStatus, Exception)}
     */
    @Test
    void testResponseError3() throws IOException {
        ResponseErrorException responseErrorException = new ResponseErrorException();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        responseErrorException.ResponseError(mockHttpServletResponse, "Header Name", HttpStatus.CONTINUE,
                new Exception("An error occurred"));
        assertEquals(100, mockHttpServletResponse.getStatus());
        assertNull(mockHttpServletResponse.getRedirectedUrl());
        assertEquals(2, mockHttpServletResponse.getHeaderNames().size());
        assertEquals("application/json", mockHttpServletResponse.getContentType());
    }
}

