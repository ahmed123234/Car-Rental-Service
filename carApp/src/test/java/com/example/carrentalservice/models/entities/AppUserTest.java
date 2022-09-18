package com.example.carrentalservice.models.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {AppUser.class})
@ExtendWith(SpringExtension.class)
class AppUserTest {

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link AppUser#AppUser()}
     *   <li>{@link AppUser#getPassword()}
     *   <li>{@link AppUser#getUsername()}
     *   <li>{@link AppUser#isAccountNonExpired()}
     *   <li>{@link AppUser#isCredentialsNonExpired()}
     * </ul>
     */
    @Test
    void testConstructor() {
        AppUser actualAppUser = new AppUser();
        assertNull(actualAppUser.getPassword());
        assertNull(actualAppUser.getUsername());
        assertTrue(actualAppUser.isAccountNonExpired());
        assertTrue(actualAppUser.isCredentialsNonExpired());
    }

    @Test
    void testConstructor1() {

        //given
        String firstName = "ahmad";
        String lastName= "ali";
        String email = "ahmad@gmail.com";
        String username = "ahmad1";
        String password = "1234";
        Collection<UserRole> roles = new ArrayList<>();
        roles.add(new UserRole(null, "ROLE_ADMIN"));

        AppUser actualAppUser = new AppUser(firstName, lastName, email, username, password, roles);
        assertEquals(password, actualAppUser.getPassword());
        assertEquals(username, actualAppUser.getUsername());
        assertEquals(roles, actualAppUser.getRoles());
        assertEquals(email, actualAppUser.getEmail());
        assertEquals(firstName, actualAppUser.getFirstName());
        assertEquals(lastName, actualAppUser.getLastName());
    }


    @Test
    void testConstructor3() {

        //given
        String firstName = "ahmad";
        String lastName= "ali";
        String email = "ahmad@gmail.com";
        String username = "ahmad1";
        String password = "1234";

        AppUser actualAppUser = new AppUser(firstName, lastName, email, username, password);
        assertEquals(password, actualAppUser.getPassword());
        assertEquals(username, actualAppUser.getUsername());
        assertEquals(email, actualAppUser.getEmail());
        assertEquals(firstName, actualAppUser.getFirstName());
        assertEquals(lastName, actualAppUser.getLastName());
    }

    /**
     * Method under test: {@link AppUser#getAuthorities()}
     */
    @Test
    void testGetAuthorities() {
        assertTrue((new AppUser()).getAuthorities().isEmpty());
    }


    /**
     * Method under test: {@link AppUser#getAuthorities()}
     */
    @Test
    void testGetAuthorities2() {
        UserRole userRole = new UserRole();
        userRole.setId(123L);
        userRole.setName("ROLE_ADMIN");

        AppUser appUser1 = new AppUser();
        appUser1.addRole(userRole);
        Collection<? extends GrantedAuthority> actualAuthorities = appUser1.getAuthorities();
        assertEquals(1, actualAuthorities.size());
        assertEquals("ROLE_ADMIN", ((List<GrantedAuthority>) actualAuthorities).get(0)
                .getAuthority());

    }

    /**
     * Method under test: {@link AppUser#isAccountNonLocked()}
     */
    @Test
    void testIsAccountNonLocked() {

        assertTrue((new AppUser()).isAccountNonLocked());
    }

    /**
     * Method under test: {@link AppUser#isAccountNonLocked()}
     */
    @Test
    void testIsAccountNonLocked2() {
        AppUser appUser1 = new AppUser();
        appUser1.setLocked(true);
        assertFalse(appUser1.isAccountNonLocked());
    }

    /**
     * Method under test: {@link AppUser#isAccountNonLocked()}
     */
    @Test
    void testIsAccountNonLocked3() {
        UserRole userRole = mock(UserRole.class);
        doNothing().when(userRole).setId(anyLong());
        doNothing().when(userRole).setName(anyString());
        userRole.setId(123L);
        userRole.setName("ROLE_ADMIN");

        AppUser appUser1 = new AppUser();
        appUser1.addRole(userRole);
        assertTrue(appUser1.isAccountNonLocked());
        verify(userRole).setId(anyLong());
        verify(userRole).setName(anyString());
    }

    /**
     * Method under test: {@link AppUser#isEnabled()}
     */
    @Test
    void testIsEnabled() {
        assertFalse((new AppUser()).isEnabled());
    }

    /**
     * Method under test: {@link AppUser#isEnabled()}
     */
    @Test
    void testIsEnabled2() {
        AppUser appUser1 = new AppUser();
        appUser1.setEnabled(true);
        assertTrue(appUser1.isEnabled());
    }

    /**
     * Method under test: {@link AppUser#addRole(UserRole)}
     */
    @Test
    void testAddRole() {
        AppUser appUser1 = new AppUser();

        UserRole userRole = new UserRole();
        userRole.setId(123L);
        userRole.setName("Name");
        appUser1.addRole(userRole);
        assertEquals(1, appUser1.getRoles().size());
    }

    /**
     * Method under test: {@link AppUser#addRole(UserRole)}
     */
    @Test
    void testAddRole2() {
        AppUser appUser1 = new AppUser();
        UserRole userRole = mock(UserRole.class);
        doNothing().when(userRole).setId(anyLong());
        doNothing().when(userRole).setName(anyString());
        userRole.setId(123L);
        userRole.setName("Name");
        appUser1.addRole(userRole);
        verify(userRole).setId(anyLong());
        verify(userRole).setName(anyString());
        assertEquals(1, appUser1.getRoles().size());
    }
}

