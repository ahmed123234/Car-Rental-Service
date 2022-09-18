package com.example.carrentalservice.models.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class DailyRentOrderTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link DailyRentOrder#DailyRentOrder()}
     *   <li>{@link DailyRentOrder#setCount(Long)}
     *   <li>{@link DailyRentOrder#setStart_date(Date)}
     *   <li>{@link DailyRentOrder#setSum(Long)}
     *   <li>{@link DailyRentOrder#toString()}
     *   <li>{@link DailyRentOrder#getCount()}
     *   <li>{@link DailyRentOrder#getSum()}
     * </ul>
     */
    @Test
    void testConstructor() {
        DailyRentOrder actualDailyRentOrder = new DailyRentOrder();
        actualDailyRentOrder.setCount(3L);
        actualDailyRentOrder.setStart_date(mock(Date.class));
        actualDailyRentOrder.setSum(1L);
        actualDailyRentOrder.toString();
        assertEquals(3L, actualDailyRentOrder.getCount().longValue());
        assertEquals(1L, actualDailyRentOrder.getSum().longValue());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link DailyRentOrder#DailyRentOrder(Long, Long, Date)}
     *   <li>{@link DailyRentOrder#setCount(Long)}
     *   <li>{@link DailyRentOrder#setStart_date(Date)}
     *   <li>{@link DailyRentOrder#setSum(Long)}
     *   <li>{@link DailyRentOrder#toString()}
     *   <li>{@link DailyRentOrder#getCount()}
     *   <li>{@link DailyRentOrder#getSum()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        DailyRentOrder actualDailyRentOrder = new DailyRentOrder(1L, 3L, mock(Date.class));
        actualDailyRentOrder.setCount(3L);
        actualDailyRentOrder.setStart_date(mock(Date.class));
        actualDailyRentOrder.setSum(1L);
        actualDailyRentOrder.toString();
        assertEquals(3L, actualDailyRentOrder.getCount().longValue());
        assertEquals(1L, actualDailyRentOrder.getSum().longValue());
    }
}

