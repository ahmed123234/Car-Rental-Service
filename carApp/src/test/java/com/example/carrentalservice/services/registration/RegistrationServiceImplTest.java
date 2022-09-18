package com.example.carrentalservice.services.registration;

import com.example.carrentalservice.configuration.security.AuthenticationProvider;
import com.example.carrentalservice.exception.ApiRequestException;
import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.ConfirmationToken;
import com.example.carrentalservice.models.handelers.AppUserRequest;
import com.example.carrentalservice.services.token.ConfirmationTokenServiceImpl;
import com.example.carrentalservice.services.user.AppUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RegistrationServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RegistrationServiceImplTest {
    @MockBean
    private AppUserServiceImpl appUserServiceImpl;

    @MockBean
    private ConfirmationTokenServiceImpl confirmationTokenServiceImpl;

    @Autowired
    private RegistrationServiceImpl registrationServiceImpl;

    /**
     * Method under test: {@link RegistrationServiceImpl#register(AppUserRequest)}
     */
    @Test
    void testRegister() {
        when(appUserServiceImpl.signUpUser(any(), any())).thenReturn("Sign Up User");
        assertEquals("http://localhost:9090/api/v1/registration/confirm?token=Sign Up User",
                registrationServiceImpl.register(new AppUserRequest(
                        "Jane",
                        "Doe",
                        "jane.doe@example.org",
                        "jane11222",
                        "201712!@qqw",
                        new String[]{"Roles"})));

        verify(appUserServiceImpl).signUpUser(any(), any());
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#register(AppUserRequest)}
     */
    @Test
    void testRegister2() {
        when(appUserServiceImpl.signUpUser(any(), any())).thenReturn("Sign Up User");
        assertThrows(IllegalArgumentException.class, () -> registrationServiceImpl.register(null));
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#register(AppUserRequest)}
     */
    @Test
    void testRegister3() {
        when(appUserServiceImpl.signUpUser(any(), any())).thenReturn("Sign Up User");
        AppUserRequest appUserRequest = mock(AppUserRequest.class);
        when(appUserRequest.getEmail()).thenThrow(new IllegalArgumentException("foo"));
        when(appUserRequest.getFirstName()).thenThrow(new IllegalArgumentException("foo"));
        when(appUserRequest.getLastName()).thenThrow(new IllegalArgumentException("foo"));
        when(appUserRequest.getPassword()).thenThrow(new IllegalArgumentException("foo"));
        when(appUserRequest.getUsername()).thenThrow(new IllegalArgumentException("foo"));
        when(appUserRequest.getRoles()).thenThrow(new IllegalArgumentException("foo"));
        assertThrows(IllegalArgumentException.class, () -> registrationServiceImpl.register(appUserRequest));
        verify(appUserRequest).getFirstName();
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#confirmToken(String)}
     */

    @Test
    void testConfirmToken() {
        //given
        String token = "ABC123";

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("password");
        appUser.setRoles(new ArrayList<>());
        appUser.setUserId(123L);
        appUser.setUsername("jane111222");

        Optional <ConfirmationToken> optional = Optional.empty();
        //when
        when(confirmationTokenServiceImpl.getToken(token)).thenReturn(optional);
        //then
        assertThatThrownBy(()->registrationServiceImpl.confirmToken(token))
                    .isInstanceOf(ApiRequestException.class)
                    .hasMessageContaining("token not found");
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#confirmToken(String)}
     */

    @Test
    void testConfirmToken2() {
        //given
        String token = "ABC123";

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("password");
        appUser.setRoles(new ArrayList<>());
        appUser.setUserId(123L);
        appUser.setUsername("jane111222");

        LocalDateTime cratedAt = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime expiresAt = LocalDateTime.of(1, 1, 1, 1, 1);

        ConfirmationToken confirmationToken = new ConfirmationToken(token, cratedAt, expiresAt, appUser);
        Optional<ConfirmationToken> optional = Optional.of(confirmationToken);
        //when
        when(confirmationTokenServiceImpl.getToken(token)).thenReturn(optional);
        LocalDateTime confirmedAt = LocalDateTime.of(1, 1, 1,1,1);
        confirmationToken.setConfirmedAt(confirmedAt);

        //then
        if (confirmationToken.getConfirmedAt().equals(confirmedAt))
            assertThatThrownBy(()->registrationServiceImpl.confirmToken(token))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("email already confirmed");
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#confirmToken(String)}
     */

    @Test
    void testConfirmToken3() {
        //given
        String token = "ABC123";

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("password");
        appUser.setRoles(new ArrayList<>());
        appUser.setUserId(123L);
        appUser.setUsername("jane111222");

        LocalDateTime cratedAt = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime expiresAt = LocalDateTime.of(2022, 9, 16, 6, 45);

        ConfirmationToken confirmationToken = new ConfirmationToken(token, cratedAt, expiresAt, appUser);
        Optional<ConfirmationToken> optional = Optional.of(confirmationToken);
        //when
        when(confirmationTokenServiceImpl.getToken(token)).thenReturn(optional);
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        //then
        if (expiredAt.isBefore(LocalDateTime.now()))
            assertThatThrownBy(()->registrationServiceImpl.confirmToken(token))
                    .isInstanceOf(ApiRequestException.class)
                    .hasMessageContaining("token expired");
    }

    /**
     * Method under test: {@link RegistrationServiceImpl#confirmToken(String)}
     */

    @Test
    void testConfirmToken4() {
        //given
        String token = "ABC123";

        AppUser appUser = new AppUser();
        appUser.setAuthenticationProvider(AuthenticationProvider.LOCAL);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("password");
        appUser.setRoles(new ArrayList<>());
        appUser.setUserId(123L);
        appUser.setUsername("jane111222");

        LocalDateTime cratedAt = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDateTime expiresAt = LocalDateTime.of(2023, 9, 17, 6,12 );

        ConfirmationToken confirmationToken = new ConfirmationToken(token, cratedAt, expiresAt, appUser);
        Optional<ConfirmationToken> optional = Optional.of(confirmationToken);
        //when
        when(confirmationTokenServiceImpl.getToken(token)).thenReturn(optional);

        String expected = "confirmed";
        when(confirmationTokenServiceImpl.setConfirmedAt(token)).thenReturn(1);


        when(appUserServiceImpl.enableAppUser(confirmationToken.getAppUser().getEmail()))
                .thenReturn("user enabled successfully");


        //then
        assertEquals(expected, registrationServiceImpl.confirmToken(token));
    }

}

