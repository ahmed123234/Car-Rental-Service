package com.example.carrentalservice.models.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class RentOrderTest {
    /**
     * Method under test: {@link RentOrder#addRentOrderItem(RentOrderItem)}
     */
    @Test
    void testAddRentOrderItem() {
        RentOrder rentOrder = new RentOrder();

        RentOrderItem rentOrderItem = new RentOrderItem();
        rentOrderItem.setCarId(123L);
        rentOrderItem.setOrderId(123L);
        rentOrderItem.setOrderItemId(123L);
        rentOrder.addRentOrderItem(rentOrderItem);
        assertEquals(1, rentOrder.getOrderItems().size());
    }

    /**
     * Method under test: {@link RentOrder#addRentOrderItem(RentOrderItem)}
     */
    @Test
    void testAddRentOrderItem2() {
        RentOrderItem rentOrderItem = new RentOrderItem();
        rentOrderItem.setCarId(123L);
        rentOrderItem.setOrderId(123L);
        rentOrderItem.setOrderItemId(123L);

        RentOrder rentOrder = new RentOrder();
        rentOrder.addRentOrderItem(rentOrderItem);

        RentOrderItem rentOrderItem1 = new RentOrderItem();
        rentOrderItem1.setCarId(123L);
        rentOrderItem1.setOrderId(123L);
        rentOrderItem1.setOrderItemId(123L);
        rentOrder.addRentOrderItem(rentOrderItem1);
        assertEquals(2, rentOrder.getOrderItems().size());
    }

    /**
     * Method under test: {@link RentOrder#RentOrder(Long, int, String, String, Date, Date)}
     */
    @Test
    void testConstructor() {
        RentOrder actualRentOrder = new RentOrder(123L, 1, "Order Status", "Order Driver", mock(Date.class),
                mock(Date.class));

        assertEquals(1, actualRentOrder.getOrderBill());
        assertEquals(123L, actualRentOrder.getUserId().longValue());
        assertEquals("Order Status", actualRentOrder.getOrderStatus());
        assertNull(actualRentOrder.getOrderItems());
        assertNull(actualRentOrder.getOrderId());
        assertEquals("Order Driver", actualRentOrder.getOrderDriver());
    }

    @Test
    void testConstructor2() {
        RentOrder actualRentOrder = new RentOrder();
        actualRentOrder.setOrderId(1L);
        actualRentOrder.setUserId(1L);
        actualRentOrder.setOrderBill(1);
        actualRentOrder.setOrderDriver("yes");
        actualRentOrder.setOrderStatus("requested");
        Set<RentOrderItem> rentOrderItemSet = new HashSet<>();
        rentOrderItemSet.add(new RentOrderItem());
        actualRentOrder.setOrderItems(rentOrderItemSet);
        Date start = new Date(20);
        Date finish = new Date(24);
        actualRentOrder.setOrderStartDate(start);
        actualRentOrder.setOrderFinishDate(finish);

        assertEquals(1L, actualRentOrder.getOrderId().longValue());
        assertEquals(1, actualRentOrder.getOrderBill());
        assertEquals(1L, actualRentOrder.getUserId().longValue());
        assertEquals("requested", actualRentOrder.getOrderStatus());
        assertEquals(rentOrderItemSet, actualRentOrder.getOrderItems());
        assertEquals(start, actualRentOrder.getOrderStartDate());
        assertEquals(finish, actualRentOrder.getOrderFinishDate());
        assertEquals("yes", actualRentOrder.getOrderDriver());
    }
}

