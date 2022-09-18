package com.example.carrentalservice.models.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class RentOrderItemTest {
    /**
     * Method under test: {@link RentOrderItem#RentOrderItem(Long, Long)}
     */
    @Test
    void testConstructor() {
        RentOrderItem actualRentOrderItem = new RentOrderItem(123L, 123L);

        assertEquals(123L, actualRentOrderItem.getCarId().longValue());
        assertNull(actualRentOrderItem.getOrderItemId());
        assertEquals(123L, actualRentOrderItem.getOrderId().longValue());
    }
}

