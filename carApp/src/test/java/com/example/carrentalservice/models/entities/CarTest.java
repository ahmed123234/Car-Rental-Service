package com.example.carrentalservice.models.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CarTest {
    /**
     * Method under test: {@link Car#Car(String, String, Long, String, String)}
     */
    @Test
    void testConstructor() {
        Car actualCar = new Car("Car Class", "Car Model", 1L, "Car Mark", "Car Status");

        assertEquals("Car Class", actualCar.getCarClass());
        assertEquals("Car Status", actualCar.getCarStatus());
        assertEquals("Car Model", actualCar.getCarModel());
        assertEquals("Car Mark", actualCar.getCarMark());
        assertNull(actualCar.getCarId());
        assertEquals(1L, actualCar.getCarCost().longValue());
    }

    @Test
    void testConstructor2() {
        Car actualCar = new Car();
        actualCar.setCarId(1L);
        actualCar.setCarStatus("status");
        actualCar.setCarClass("class");
        actualCar.setCarMark("mark");
        actualCar.setCarModel("model");
        actualCar.setCarCost(10L);

        assertEquals("class", actualCar.getCarClass());
        assertEquals("status", actualCar.getCarStatus());
        assertEquals("model", actualCar.getCarModel());
        assertEquals("mark", actualCar.getCarMark());
        assertEquals(1L, actualCar.getCarId());
        assertEquals(10L, actualCar.getCarCost().longValue());
    }

    @Test
    void testConstructor3() {
        Car actualCar  =new Car(1L, "Car Class", "Car Model",
                "Car Mark", 1L, "Car Status");

        assertEquals("Car Class", actualCar.getCarClass());
        assertEquals("Car Status", actualCar.getCarStatus());
        assertEquals("Car Model", actualCar.getCarModel());
        assertEquals("Car Mark", actualCar.getCarMark());
        assertEquals(1L, actualCar.getCarId());
        assertEquals(1L, actualCar.getCarCost().longValue());
    }
}

