package com.example.carrentalservice.models.handelers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppUserRequestTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link AppUserRequest#AppUserRequest(String, String, String, String, String, String[])}
     *   <li>{@link AppUserRequest#toString()}
     *   <li>{@link AppUserRequest#getEmail()}
     *   <li>{@link AppUserRequest#getFirstName()}
     *   <li>{@link AppUserRequest#getLastName()}
     *   <li>{@link AppUserRequest#getPassword()}
     *   <li>{@link AppUserRequest#getUsername()}
     * </ul>
     */
    @Test
    void testConstructor() {
        AppUserRequest actualAppUserRequest = new AppUserRequest("Jane", "Doe", "jane.doe@example.org", "janedoe",
                "iloveyou", new String[]{"Roles"});
        String actualToStringResult = actualAppUserRequest.toString();
        assertEquals("jane.doe@example.org", actualAppUserRequest.getEmail());
        assertEquals("Jane", actualAppUserRequest.getFirstName());
        assertEquals("Doe", actualAppUserRequest.getLastName());
        assertEquals("iloveyou", actualAppUserRequest.getPassword());
        assertEquals("janedoe", actualAppUserRequest.getUsername());
        assertEquals(
                "AppUserRequest(firstName=Jane, lastName=Doe, email=jane.doe@example.org, username=janedoe, password=iloveyou,"
                        + " roles=[Roles])",
                actualToStringResult);
    }
}

