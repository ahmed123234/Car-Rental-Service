package com.example.carrentalservice.models.handelers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class RestResponseTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link RestResponse#RestResponse(ObjectNode, HttpStatus, ZonedDateTime)}
     *   <li>{@link RestResponse#setData(ObjectNode)}
     *   <li>{@link RestResponse#setStatus(HttpStatus)}
     *   <li>{@link RestResponse#setZonedDateTime(ZonedDateTime)}
     *   <li>{@link RestResponse#getData()}
     *   <li>{@link RestResponse#getStatus()}
     *   <li>{@link RestResponse#getZonedDateTime()}
     * </ul>
     */
    @Test
    void testConstructor() {
        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.withExactBigDecimals(true));
        RestResponse actualRestResponse = new RestResponse(objectNode, HttpStatus.CONTINUE, null);
        ObjectNode objectNode1 = new ObjectNode(JsonNodeFactory.withExactBigDecimals(true));
        actualRestResponse.setData(objectNode1);
        actualRestResponse.setStatus(HttpStatus.CONTINUE);
        actualRestResponse.setZonedDateTime(null);
        ObjectNode data = actualRestResponse.getData();
        assertSame(objectNode1, data);
        assertEquals(objectNode, data);
        assertEquals(HttpStatus.CONTINUE, actualRestResponse.getStatus());
        assertNull(actualRestResponse.getZonedDateTime());
    }
}

