package com.example.carrentalservice.models.handelers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class RentOrderRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link RentOrderRequest#RentOrderRequest(String, Date, Date)}
     *   <li>{@link RentOrderRequest#setOrderDriver(String)}
     *   <li>{@link RentOrderRequest#setOrderFinishDate(Date)}
     *   <li>{@link RentOrderRequest#setOrderStartDate(Date)}
     *   <li>{@link RentOrderRequest#getOrderDriver()}
     * </ul>
     */
    @Test
    void testConstructor() {
        RentOrderRequest actualRentOrderRequest = new RentOrderRequest("Order Driver", mock(Date.class), mock(Date.class));
        actualRentOrderRequest.setOrderDriver("Order Driver");
        Date startDate = new Date(20);
        Date finishDate = new Date(24);
        actualRentOrderRequest.setOrderFinishDate(finishDate);
        actualRentOrderRequest.setOrderStartDate(startDate);
        assertEquals("Order Driver", actualRentOrderRequest.getOrderDriver());
        assertSame(startDate, actualRentOrderRequest.getOrderStartDate());
        assertSame(finishDate, actualRentOrderRequest.getOrderFinishDate());
    }
}

