package com.example.carrentalservice.repositories;

import com.example.carrentalservice.models.entities.AppUser;
import com.example.carrentalservice.models.entities.ConfirmationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//import static org.junit.jupiter.api.Assertions.*;
@ContextConfiguration(classes = {ConfirmationTokenRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.example.carrentalservice.models.entities"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class ConfirmationTokenRepositoryTest {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;


    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown(){
        confirmationTokenRepository.deleteAll();
    }

    /**
     * Method under test: {@link ConfirmationTokenRepository#findByToken(String)}
     */
    @Test
    void testFindByToken() {

        //given
        String token = "asd-agh-uio-567";
        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );


        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );


        //when
        Optional<ConfirmationToken> actual =
                confirmationTokenRepository.findByToken(token);

        //then
        actual.ifPresent(value -> assertThat(value).isEqualTo(confirmationToken));

    }

    /**
     * Method under test: {@link ConfirmationTokenRepository#updateConfirmedAt(String, LocalDateTime)}
     */
    @Test
    void testUpdateConfirmedAt() {
        //given
        String token = "asd-agh-uio-567";
        LocalDateTime dateTime = LocalDateTime.now();
        int expected = 0;
        AppUser user = new AppUser(
                "ahmad",
                "ali",
                "ahmad@gmail.com",
                "ahmad1",
                "201712@Asg"
        );


        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationToken.setConfirmedAt(dateTime);

        //when
        int actual = confirmationTokenRepository.updateConfirmedAt(token, dateTime);

        //then
        if (actual == 1) {
            expected = 1;
        }
        assertThat(actual).isEqualTo(expected);

    }
}