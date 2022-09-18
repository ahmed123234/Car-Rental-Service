package com.example.carrentalservice.models.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserRoleTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserRole#UserRole()}
     *   <li>{@link UserRole#setId(Long)}
     *   <li>{@link UserRole#setName(String)}
     *   <li>{@link UserRole#getId()}
     *   <li>{@link UserRole#getName()}
     * </ul>
     */
    @Test
    void testConstructor() {
        UserRole actualUserRole = new UserRole();
        actualUserRole.setId(123L);
        actualUserRole.setName("Name");
        assertEquals(123L, actualUserRole.getId().longValue());
        assertEquals("Name", actualUserRole.getName());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserRole#UserRole(Long, String)}
     *   <li>{@link UserRole#setId(Long)}
     *   <li>{@link UserRole#setName(String)}
     *   <li>{@link UserRole#getId()}
     *   <li>{@link UserRole#getName()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        UserRole actualUserRole = new UserRole(123L, "Name");
        actualUserRole.setId(123L);
        actualUserRole.setName("Name");
        assertEquals(123L, actualUserRole.getId().longValue());
        assertEquals("Name", actualUserRole.getName());
    }
}

