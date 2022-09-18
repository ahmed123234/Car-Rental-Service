package com.example.carrentalservice.services.token;

import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.ConfirmationToken;
import com.example.carrentalservice.repositories.ConfirmationTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ConfirmationTokenServiceImpl.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.carrentalservice.models.entities"})
@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceImplTest {

    @Autowired
    private ConfirmationTokenServiceImpl confirmationTokenServiceImpl;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    private ConfirmationTokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new ConfirmationTokenServiceImpl(confirmationTokenRepository);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Method under test: {@link ConfirmationTokenServiceImpl#saveConfirmationToken(ConfirmationToken)}
     */
    @Test
    void testSaveConfirmationToken() {
        //given
        ConfirmationToken confirmationToken = new ConfirmationToken(
                "sdd-h123-oo",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                new AppUser(
                        "ahmad",
                        "ali",
                        "ahmad@gmail.com",
                        "ahmad1",
                        "201712@Asg"
                )
        );

        //when
        tokenService.saveConfirmationToken(confirmationToken);

        //then
        verify(confirmationTokenRepository).save(confirmationToken);
    }

    /**
     * Method under test: {@link ConfirmationTokenServiceImpl#getToken(String)}
     */
    @Test
    void testGetToken() {
        //given
        String token = "sdd-h123-oo";

        //when
        tokenService.getToken(token);

        //then
        verify(confirmationTokenRepository).findByToken(token);
    }

    /**
     * Method under test: {@link ConfirmationTokenServiceImpl#setConfirmedAt(String)}
     */
    @Test
    void testSetConfirmedAt() {
        //given
        String token = "sdd-h123-oo";

        //when
        tokenService.setConfirmedAt(token);

        //then
        verify(confirmationTokenRepository).updateConfirmedAt(token,
                LocalDateTime.now());
    }

    /**
     * Method under test: {@link ConfirmationTokenServiceImpl#setConfirmedAt(String)}
     */
    @Test
    void testSetConfirmedAt2() {
        when(confirmationTokenRepository.updateConfirmedAt((String) any(), (LocalDateTime) any())).thenReturn(1);
        assertEquals(1, confirmationTokenServiceImpl.setConfirmedAt("ABC123"));
        verify(confirmationTokenRepository).updateConfirmedAt((String) any(), (LocalDateTime) any());
    }
}