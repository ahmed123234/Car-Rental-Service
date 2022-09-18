package com.example.carrentalservice.models.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

class ConfirmationTokenTest {
    /**
     * Method under test: {@link ConfirmationToken#ConfirmationToken(String, LocalDateTime, LocalDateTime)}
     */
    @Test
    void testConstructor() {
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime ofResult1 = LocalDateTime.of(1, 1, 1, 1, 1);
        ConfirmationToken actualConfirmationToken = new ConfirmationToken("ABC123", ofResult, ofResult1);

        assertNull(actualConfirmationToken.getAppUser());
        assertEquals("ABC123", actualConfirmationToken.getToken());
        assertNull(actualConfirmationToken.getId());
        LocalDateTime expiresAt = actualConfirmationToken.getExpiresAt();
        assertSame(ofResult1, expiresAt);
        LocalDateTime createdAt = actualConfirmationToken.getCreatedAt();
        assertSame(ofResult, createdAt);
        assertEquals("01:01", expiresAt.toLocalTime().toString());
        assertEquals("01:01", createdAt.toLocalTime().toString());
        assertEquals("0001-01-01", createdAt.toLocalDate().toString());
        assertEquals("0001-01-01", expiresAt.toLocalDate().toString());
        assertNull(actualConfirmationToken.getConfirmedAt());
    }

    /**
     * Method under test: {@link ConfirmationToken#ConfirmationToken(String, LocalDateTime, LocalDateTime, AppUser)}
     */
    @Test
    void testConstructor2() {
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime ofResult1 = LocalDateTime.of(1, 1, 1, 1, 1);

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("password");
        ArrayList<UserRole> userRoleList = new ArrayList<>();
        appUser.setRoles(userRoleList);
        appUser.setUserId(123L);
        appUser.setUsername("ahmad");
        ConfirmationToken actualConfirmationToken = new ConfirmationToken("ABC123", ofResult, ofResult1, appUser);

        AppUser appUser1 = actualConfirmationToken.getAppUser();
        assertSame(appUser, appUser1);
        assertEquals("ABC123", actualConfirmationToken.getToken());
        assertNull(actualConfirmationToken.getConfirmedAt());
        LocalDateTime expiresAt = actualConfirmationToken.getExpiresAt();
        assertSame(ofResult1, expiresAt);
        assertNull(actualConfirmationToken.getId());
        assertEquals("0001-01-01", expiresAt.toLocalDate().toString());
        assertEquals("01:01", expiresAt.toLocalTime().toString());
        LocalDateTime createdAt = actualConfirmationToken.getCreatedAt();
        assertSame(ofResult, createdAt);
        assertEquals("0001-01-01", createdAt.toLocalDate().toString());
        assertEquals("01:01", createdAt.toLocalTime().toString());
        Collection<? extends GrantedAuthority> authorities = appUser1.getAuthorities();
        assertEquals(userRoleList, authorities);
        assertTrue(authorities.isEmpty());
        assertEquals("password", appUser1.getPassword());
        assertTrue(appUser1.getLocked());
        assertEquals("Doe", appUser1.getLastName());
        assertEquals("Jane", appUser1.getFirstName());
        assertTrue(appUser1.getEnabled());
        assertEquals("ahmad", appUser1.getUsername());
        Collection<UserRole> roles = appUser1.getRoles();
        assertSame(userRoleList, roles);
        assertEquals(authorities, roles);
        assertTrue(roles.isEmpty());
        assertEquals(AuthenticationProvider.LOCAL, appUser1.getAuthenticationProvider());
        assertTrue(appUser1.isAccountNonExpired());
        assertTrue(appUser1.isCredentialsNonExpired());
        assertEquals(123L, appUser1.getUserId());
        assertEquals("jane.doe@example.org", appUser1.getEmail());
    }


    @Test
    void testConstructor3() {
        LocalDateTime ofResult = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime ofResult1 = LocalDateTime.of(1, 1, 1, 1, 1);
        ConfirmationToken actualConfirmationToken = new ConfirmationToken();
        actualConfirmationToken.setConfirmedAt(ofResult);
        actualConfirmationToken.setCreatedAt(ofResult1);
        actualConfirmationToken.setToken("token");
        actualConfirmationToken.setId(1L);
        AppUser user = new AppUser();
        actualConfirmationToken.setAppUser(user);
        actualConfirmationToken.setExpiresAt(ofResult);

        assertEquals(user, actualConfirmationToken.getAppUser());
        assertEquals("token", actualConfirmationToken.getToken());
        assertEquals(1L, actualConfirmationToken.getId());
        LocalDateTime expiresAt = actualConfirmationToken.getExpiresAt();
        assertSame(ofResult, expiresAt);
        LocalDateTime createdAt = actualConfirmationToken.getCreatedAt();
        assertSame(ofResult1, createdAt);
        assertEquals("01:01", expiresAt.toLocalTime().toString());
        assertEquals("01:01", createdAt.toLocalTime().toString());
        assertEquals("0001-01-01", createdAt.toLocalDate().toString());
        assertEquals("0001-01-01", expiresAt.toLocalDate().toString());
        assertEquals(ofResult, actualConfirmationToken.getConfirmedAt());
    }
}

