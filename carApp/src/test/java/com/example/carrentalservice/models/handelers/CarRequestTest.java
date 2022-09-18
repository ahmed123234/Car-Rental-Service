package com.example.carrentalservice.models.handelers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CarRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CarRequest#CarRequest(String, String, Long, String)}
     *   <li>{@link CarRequest#toString()}
     *   <li>{@link CarRequest#getCarClass()}
     *   <li>{@link CarRequest#getCarCost()}
     *   <li>{@link CarRequest#getCarMark()}
     *   <li>{@link CarRequest#getCarModel()}
     * </ul>
     */
    @Test
    void testConstructor() {
        CarRequest actualCarRequest = new CarRequest("Car Class", "Car Model", 1L, "Car Mark");
        String actualToStringResult = actualCarRequest.toString();
        assertEquals("Car Class", actualCarRequest.getCarClass());
        assertEquals(1L, actualCarRequest.getCarCost().longValue());
        assertEquals("Car Mark", actualCarRequest.getCarMark());
        assertEquals("Car Model", actualCarRequest.getCarModel());
        assertEquals("CarRequest(carClass=Car Class, carModel=Car Model, carCost=1, carMark=Car Mark)",
                actualToStringResult);
    }
}

